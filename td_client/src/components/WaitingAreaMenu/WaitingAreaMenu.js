import ClickBox from "../ClickBox/ClickBox";
import '../WhiteMenu/WhiteMenu.css';
import {createSearchParams, useNavigate} from "react-router-dom"
import {useContext, useEffect, useState} from "react";
import {WebSocketContext} from "../WebsocketContext/WebsocketContext";
import {parse} from "tinyduration";
import 'react-toastify/dist/ReactToastify.css';
import {toast, ToastContainer} from "react-toastify";

/**
 * UI for the menu of the Waiting Area.
 * @return {JSX.Element} The menu for the Waiting Area.
 */
function WaitingAreaMenu() {
    const navigate = useNavigate();
    const {receivedMessage, id, sendJsonMessage} = useContext(WebSocketContext);
    const [sentCheckout, setSentCheckout] = useState(false);
    const [position, setPosition] = useState(receivedMessage.position)
    const [eta, setETA] = useState(parse(receivedMessage.estimatedTime))

    //Notification when the clients position has changed.
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
            theme: "dark",
        });
    }

    //Send update state request every 50s.
    useEffect(() => {
        const interval = setInterval(() => {
            sendJsonMessage({
                "type": "state-update",
                "truckID": id
            });
        }, 50000);
        return () => {
            clearInterval(interval);
        };
    }, []);

    //Handles server response.
    useEffect(() => {
        console.log("WAITING AREA");
        console.log(receivedMessage);

        if (receivedMessage.locationState === "waiting_area") {
            //Update and notify client when their position in the waiting area has updated.
            setPosition(receivedMessage.position);
            setETA(parse(receivedMessage.estimatedTime));
            notify();
        } else if (receivedMessage.locationState === "docking_area") {
            //Move client to docking area.
            navigate("/DockingArea");
        } else {
            //Client has been kicked or cancelled.
            navigate({
                pathname: "/",
                search: createSearchParams({
                    wasKicked: !sentCheckout
                }).toString()
            });
        }
    }, [receivedMessage]);

    //Handles when client sends a check out request.
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
            <hr className={"Divider Divider-blue"}/>
            <div className="WhiteMenu-header WhiteMenu-subheader">Truck ID: {id}</div>
            <hr className={"Divider Divider-blue"}/>
            <div className="WhiteMenu-header WhiteMenu-subheader">
                Estimated Time:
                <div>
                    {eta.hours || "0"} HRS {eta.minutes || "0"} MIN
                </div>
            </div>
            <form onSubmit={submitHandler}>
                <ClickBox text={"Cancel"}/>
            </form>
        </div>
    );
}

export default WaitingAreaMenu;
