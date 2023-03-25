import "./WarehouseStateMenu.css"
import "../WhiteMenu/WhiteMenu.css"
import {DragDropContext, Draggable, Droppable} from "react-beautiful-dnd";
import DraggableList from "../DraggableList/DraggableList";
import List from "../List/List";

const warehouseState = {
    warehouseStateList: {
        dockingAreaList: [
            {
                id: 1,
                driverName: "DIE"
            },
            {
                id: 2,
                driverName: "THIS IS DOCKING"
            },
            {
                id: 3,
                driverName: "SWAG"
            }
        ],
        waitingAreaList: [
            {
                id: 4,
                driverName: "IM WAITING"
            },
            {
                id: 5,
                driverName: "OK"
            },
            {
                id: 6,
                driverName: "SWAG"
            }
        ]
    },
    getWarehouseState: function () {
        return (
            (localStorage.getItem("warehouseState") && JSON.parse(localStorage.getItem("warehouseState"))) || this.warehouseStateList
        );
    },
    saveWarehouseState: function (list) {
        localStorage.setItem("warehouseState", JSON.stringify(list));
    }
}
//    localStorage.setItem("warehouseState", JSON.stringify(warehouseState.warehouseStateList))

function WarehouseStateMenu() {
    const list = warehouseState.getWarehouseState();
    const waitingAreaList = list.waitingAreaList;
    const dockingAreaList = list.dockingAreaList;
    console.log(warehouseState.getWarehouseState())
    return (
        <div className={"WarehouseStateMenu"}>
            <div className={"WarehouseStateMenu-title"}>Warehouse State</div>
            <div className={"Divider"}></div>
            <List list={dockingAreaList} title={"Docking Area"}/>
            <DraggableList list={waitingAreaList} title={"Waiting Area"} warehouseState={list}/>
        </div>
    );
}

export default WarehouseStateMenu;