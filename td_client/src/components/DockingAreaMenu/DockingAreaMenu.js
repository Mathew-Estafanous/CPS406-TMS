import ClickBox from "../ClickBox/ClickBox";
import ElapsedTime from "../ElapsedTime/ElapsedTime";
import '../WhiteMenu/WhiteMenu.css';
import { useNavigate } from "react-router-dom"
import {useContext, useEffect, useState} from "react";
import {parse} from "tinyduration";
import {WebSocketContext} from "../WebsocketContext/WebsocketContext";

function DockingAreaMenu() {
    const navigate = useNavigate();
    const {sendJsonMessage, id, receivedMessage} = useContext(WebSocketContext);

    const [position, setPosition] = useState(receivedMessage.position);
    const [eta, setETA] = useState(parse(receivedMessage.estimatedTime));

    useEffect(() => {
        console.log("DOCKING AREA");
        console.log(receivedMessage);
        if (receivedMessage.type === "state_update") {
            setPosition(receivedMessage.position)
            setETA(receivedMessage.estimatedTime)
        } else if (receivedMessage.locationState === "leaving") {
            navigate("/");
        }
    }, [receivedMessage])

    const submitHandler = (_) => {
        let message = {
            "type": "check-out",
            "truckID": id
        }
        sendJsonMessage(message);
    }

    return (
        <div>
            <div className="WhiteMenu-title">Docking Area</div>
            <div className="WhiteMenu-header WhiteMenu-subheader">Assigned Docking #: {position}</div>
            <hr className="Divider" />
            <div className="WhiteMenu-header WhiteMenu-subheader">
                Elapsed Time:
                <div>
                    <ElapsedTime />
                </div>
            </div>
            <form onSubmit={submitHandler}>
                <ClickBox text={"Check Out"}/>
            </form>
        </div>
    );
}

export default DockingAreaMenu;
