import ClickBox from "../ClickBox/ClickBox";
import ElapsedTime from "../ElapsedTime/ElapsedTime";
import '../WhiteMenu/WhiteMenu.css';
import {createSearchParams, useNavigate} from "react-router-dom"
import {useContext, useEffect, useState} from "react";
import {parse} from "tinyduration";
import {WebSocketContext} from "../WebsocketContext/WebsocketContext";

/**
 * Menu UI when Truck Driver is in Docking Area
 * @return {JSX.Element} Docking Area menu.
 */
function DockingAreaMenu() {
    const navigate = useNavigate();
    const {sendJsonMessage, id, receivedMessage} = useContext(WebSocketContext);
    const [sentCheckout, setSentCheckout] = useState(false);
    const [position, setPosition] = useState(receivedMessage.position);
    const [eta, setETA] = useState(parse(receivedMessage.estimatedTime));

    //Handles messages sent by server.
    useEffect(() => {
        console.log("DOCKING AREA");
        console.log(receivedMessage);
        if (receivedMessage.locationState === "docking_area") {
            setPosition(receivedMessage.position)
            setETA(parse(receivedMessage.estimatedTime))
        } else {
            sessionStorage.setItem("dockingAreaTime", null);
            navigate({
                pathname: "/",
                search: createSearchParams({
                    wasKicked: !sentCheckout
                }).toString()
            });
        }
    }, [receivedMessage])

    //Handles request for checking out.
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
            <div className="WhiteMenu-title">Docking Area</div>
            <div className="WhiteMenu-header WhiteMenu-subheader">Assigned Docking #: {position}</div>
            <hr className={"Divider Divider-blue"}/>
            <div className="WhiteMenu-header WhiteMenu-subheader">Truck ID: {id}</div>
            <hr className={"Divider Divider-blue"}/>
            <div className="WhiteMenu-header WhiteMenu-subheader">
                Estimated Time:
                <div>
                    {eta.hours || "0"} HRS {eta.minutes || "0"} MIN
                </div>
            </div>
            <hr className={"Divider Divider-blue"}/>
            <div className="WhiteMenu-header WhiteMenu-subheader">
                Elapsed Time:
                <div>
                    <ElapsedTime eta={eta}/>
                </div>
            </div>
            <form onSubmit={submitHandler}>
                <ClickBox text={"Check Out"}/>
            </form>
        </div>
    );
}

export default DockingAreaMenu;
