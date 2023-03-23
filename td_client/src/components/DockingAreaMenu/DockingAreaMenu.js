import ClickBox from "../ClickBox/ClickBox";
import '../WhiteMenu/WhiteMenu.css';
import { useNavigate } from "react-router-dom"
import {useContext, useEffect, useState} from "react";
import {parse} from "tinyduration";
import {WebSocketContext} from "../WebsocketContext/WebsocketContext";

function DockingAreaMenu() {
    const navigate = useNavigate();
    const {sendJsonMessage, id, receivedMessage} = useContext(WebSocketContext);

    const [position, setPosition] = useState(receivedMessage.position);
    console.log(receivedMessage.estimatedTime);
    const [eta, setETA] = useState(parse(receivedMessage.estimatedTime));

    useEffect(() => {
        if (receivedMessage.type === "state_update") {
            setPosition(receivedMessage.position)
            setETA(receivedMessage.estimatedTime)
        } else if (receivedMessage.type === "check_out") {
            navigate("/");
        }
    }, [receivedMessage])

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
            <div className="WhiteMenu-title">Docking Area</div>
            <div className="WhiteMenu-header WhiteMenu-subheader">Assigned Docking #: {position}</div>
            <hr className="Divider" />
            <div className="WhiteMenu-header WhiteMenu-subheader">
                <div>
                    Estimated Time:
                    <div>
                        {eta.hours || "00"} HRS {eta.minutes || "00"} MIN
                    </div>
                </div>
            </div>
            <form onSubmit={submitHandler}>
                <ClickBox text={"Check Out"}/>
            </form>
        </div>
    );
}

export default DockingAreaMenu;
