import "./WarehouseStateMenu.css"
import "../WhiteMenu/WhiteMenu.css"
import DraggableList from "../DraggableList/DraggableList";
import List from "../List/List";
import {useContext, useEffect, useState} from "react";
import {AdminWebSocketContext} from "../WebsocketContext/AdminWebsocketContext";
import {useNavigate} from "react-router-dom";

let initialState = {
    dockingAreaList: [],
    waitingAreaList: []
}
function WarehouseStateMenu() {
    const { sendJsonMessage, receivedMessage, readyState } = useContext(AdminWebSocketContext);
    const [state, setState] = useState(initialState)
    const navigate = useNavigate();

    useEffect(() => {
        console.log("RETRIEVING STATE")
        sendJsonMessage({"type": "view_state"})
    }, [readyState]);

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
            console.log(wState);
            setState(wState)
        } else if (receivedMessage.type === "change_position") {
            console.log("SUCCESSFUL CHANGE")
        } else if (receivedMessage.type === "failed") {
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
    return (
        <div className={"WarehouseStateMenu"}>
            <div className={"WarehouseStateMenu-title"}>Warehouse State</div>
            <div className={"Divider"}></div>
            <div className={"box"}>
                <List list={state.dockingAreaList} title={"Docking Area"}/>
                <DraggableList list={state.waitingAreaList} title={"Waiting Area"} sendRepositionCommand={sendRepositionCommand} />
            </div>
        </div>
    );
}

export default WarehouseStateMenu;