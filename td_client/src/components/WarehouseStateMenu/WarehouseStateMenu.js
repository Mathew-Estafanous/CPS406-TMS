import "./WarehouseStateMenu.css"
import "../WhiteMenu/WhiteMenu.css"
import DraggableList from "../DraggableList/DraggableList";
import List from "../List/List";
import {useContext, useEffect, useState} from "react";
import {AdminWebSocketContext} from "../WebsocketContext/AdminWebsocketContext";
import {useNavigate} from "react-router-dom";
import {toast, ToastContainer} from "react-toastify";

let initialState = {
    dockingAreaList: [],
    waitingAreaList: []
}

let notificationOptions = {
    position: "top-left",
    autoClose: 1500,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
    theme: "light",
}

function WarehouseStateMenu() {
    const { sendJsonMessage, receivedMessage} = useContext(AdminWebSocketContext);
    const [state, setState] = useState(initialState)
    const navigate = useNavigate();

    useEffect(() => {
        const interval = setInterval(() => {
            sendJsonMessage({"type": "view_state"})
        }, 1000);
        return () => {
            clearInterval(interval);
        };
    }, []);

    useEffect(() => {
        if (Array.isArray(receivedMessage)) {
            let wState = initialState;
            receivedMessage.forEach((item) => {
                if (item.locationState === "docking_area") {
                    wState = {
                        dockingAreaList: [...wState.dockingAreaList, item],
                        waitingAreaList: wState.waitingAreaList
                    }
                } else {
                    wState = {
                        waitingAreaList: [...wState.waitingAreaList, item],
                        dockingAreaList: wState.dockingAreaList
                    }
                }
            });
            setState(wState)
        } else if (receivedMessage.type === "change_position") {
            toast.success(`Success: Truck ID #${receivedMessage.truckID} moved`, notificationOptions);
        } else if (receivedMessage.type === "cancel") {
            toast.success(`Success: Cancelled Truck ID #${receivedMessage.truckID}`, notificationOptions);
        }else if (receivedMessage.type === "failed") {
            navigate("/AdminLogin")
        }
    }, [receivedMessage]);

    const sendRepositionCommand = (id, newPosition, sourceIdx) => {
        let repositionMessage = {
            "type": "change_position",
            "truckID": id,
            "position": newPosition
        }
        sendJsonMessage(repositionMessage);
        let newState = [...state.waitingAreaList]
        newState.splice(newPosition-1, 0, newState.splice(sourceIdx, 1)[0]);
        setState({
            waitingAreaList: newState,
            dockingAreaList: state.dockingAreaList
        });
    }

    const sendCancelCommand = (id) => {
        console.log("CANCELLINGG")
        let cancelMessage = {
            "type": "cancel",
            "truckID": id
        }
        sendJsonMessage(cancelMessage)
    }

    return (
        <div className={"WarehouseStateMenu"}>
            <ToastContainer />
            <div className={"WarehouseStateMenu-title"}>Warehouse State</div>
            <div className={"Divider"}></div>
            <div className={"box"}>
                <List list={state.dockingAreaList} title={"Docking Area"} cancelCommand={sendCancelCommand}/>
                <DraggableList list={state.waitingAreaList}
                               title={"Waiting Area"}
                               sendRepositionCommand={sendRepositionCommand}
                               cancelCommand={sendCancelCommand}/>
            </div>
        </div>
    );
}

export default WarehouseStateMenu;