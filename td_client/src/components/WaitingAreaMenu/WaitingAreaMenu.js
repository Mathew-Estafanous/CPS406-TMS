import ClickBox from "../ClickBox/ClickBox";
import '../WhiteMenu/WhiteMenu.css';
import {createSearchParams, useNavigate} from "react-router-dom"
import {useContext, useEffect, useState} from "react";
import {WebSocketContext} from "../WebsocketContext/WebsocketContext";
import { parse } from "tinyduration";
import 'react-toastify/dist/ReactToastify.css';
import {toast, ToastContainer} from "react-toastify";

function WaitingAreaMenu() {
    //Check out just takes you back to the Check In for now
    const navigate = useNavigate();
    const { receivedMessage, id, sendJsonMessage } = useContext(WebSocketContext);

    const [sentCheckout, setSentCheckout] = useState(false);
    const [position, setPosition] = useState(receivedMessage.position)
    const [eta, setETA] = useState(parse(receivedMessage.estimatedTime))

    let notify = () => {
        toast.info('Your Position Has Updated', {
            toastId: 1,
            position: "top-left",
            autoClose: 1500,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "light",
        });
    }

    useEffect(() => {
        console.log("WAITING AREA");
        console.log(receivedMessage);
        if (receivedMessage.locationState === "waiting_area") {
            setPosition(receivedMessage.position);
            setETA(parse(receivedMessage.estimatedTime));
            notify();
        } else if (receivedMessage.locationState === "docking_area") {
            navigate("/DockingArea");
        } else {
            navigate({
                pathname: "/",
                search: createSearchParams({
                    wasKicked: !sentCheckout
                }).toString()
            });
        }
    }, [receivedMessage]);

    const submitHandler = (e) => {
        e.preventDefault();
        let message = {
            "type": "check-out",
            "truckID": id
        }
        setSentCheckout(true);
        sendJsonMessage(message);
    }

    return (
        <div>
            <ToastContainer limit={1}/>
            <div className="WhiteMenu-title">Waiting Area:</div>
            <div className="WhiteMenu-header WhiteMenu-subheader">Position Number: {position}</div>
            <hr className="Divider"/>
            <div className="WhiteMenu-header WhiteMenu-subheader">Truck ID: {id}</div>
            <hr className="Divider" />
            <div className="WhiteMenu-header WhiteMenu-subheader">
                Estimated Time:
                <div>
                    {eta.hours || "0"} HRS { eta.minutes || "0"} MIN
                </div>
            </div>
            <form onSubmit={submitHandler}>
                <ClickBox text={"Cancel"}/>
            </form>
        </div>
    );
}

export default WaitingAreaMenu;
