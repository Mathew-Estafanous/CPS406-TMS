import ClickBox from "../ClickBox/ClickBox";
import '../WhiteMenu/WhiteMenu.css';
import { useNavigate } from "react-router-dom"
import {useContext, useEffect, useState} from "react";
import {WebSocketContext} from "../WebsocketContext/WebsocketContext";
import { parse } from "tinyduration";
import { toast } from "react-toastify";

function WaitingAreaMenu() {
    //Check out just takes you back to the Check In for now
    const navigate = useNavigate();
    const { receivedMessage, id, sendJsonMessage } = useContext(WebSocketContext);

    const [position, setPosition] = useState(receivedMessage.position)
    const [eta, setETA] = useState(parse(receivedMessage.estimatedTime))

    useEffect(() => {
        console.log("WAITING AREA");
        console.log(receivedMessage);
        if (receivedMessage.type !== "state_update") return;
        if (receivedMessage.locationState === "waiting_area") {
            setPosition(receivedMessage.position);
            setETA(parse(receivedMessage.estimatedTime));
            toast.info('Your Position Has Updated', {
                position: "top-left",
                autoClose: 1500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            });
        } else if (receivedMessage.locationState === "docking_area") {
            navigate("/DockingArea");
        }
    }, [receivedMessage]);

    const submitHandler = (_) => {
        let message = {
            "type": "check-out",
            "truckID": id
        }
        sendJsonMessage(message);
        navigate("/");
    }

    return (
        <div>
            <div className="WhiteMenu-title">Waiting Area:</div>
            <div className="WhiteMenu-header WhiteMenu-subheader">Position Number: {position}</div>
            <hr className="Divider"/>
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
