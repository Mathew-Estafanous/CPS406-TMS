import "./WarehouseStateMenu.css"
import "../WhiteMenu/WhiteMenu.css"
import {DragDropContext, Draggable, Droppable} from "react-beautiful-dnd";

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


function WarehouseStateMenu() {
    const list = warehouseState.getWarehouseState();
    const waitingAreaList = list.waitingAreaList;
    const dockingAreaList = list.dockingAreaList;
    return (
        <div className={"WarehouseStateMenu"}>
            <div className={"WarehouseStateMenu-title"}>Warehouse State</div>
            <div className={"Divider"}></div>
            <div className={"WarehouseStateMenu-listContainer"}>
                <DragDropContext onDragEnd={(dragItem) => {
                    const sourceIndex = dragItem.source.index;
                    const destIndex = dragItem.destination.index;
                    dockingAreaList.splice(destIndex, 0, dockingAreaList.splice(sourceIndex, 1)[0]);
                }}>
                    <Droppable droppableId={"droppable-1"}>
                        {(provided, _) => (
                            <div ref={provided.innerRef} {...provided.droppableProps}>
                                <div className={"WarehouseStateMenu-header"}>Docking Area</div>
                                {dockingAreaList.map((item, i) => (
                                    <Draggable key={item.id} draggableId={"draggable-" + item.id} index={i}>
                                        {(provided, _) => (
                                            <div className={"WarehouseStateMenu-listItem"}
                                                 ref={provided.innerRef} {...provided.draggableProps} {...provided.dragHandleProps}>
                                                {item.driverName}
                                            </div>
                                        )}
                                    </Draggable>
                                ))}
                                {provided.placeholder}
                                .
                            </div>
                        )}
                    </Droppable>
                </DragDropContext>
            </div>
            <div className={"WarehouseStateMenu-listContainer"}>
                <DragDropContext onDragEnd={(dragItem) => {
                    const sourceIndex = dragItem.source.index;
                    const destIndex = dragItem.destination.index;
                    waitingAreaList.splice(destIndex, 0, waitingAreaList.splice(sourceIndex, 1)[0]);
                }}>
                    <Droppable droppableId={"droppable-2"}>
                        {(provided, _) => (
                            <div ref={provided.innerRef} {...provided.droppableProps}>
                                <div className={"WarehouseStateMenu-header"}>Waiting Area</div>
                                {waitingAreaList.map((item, i) => (
                                    <Draggable key={item.id} draggableId={"draggable-" + item.id} index={i}>
                                        {(provided, _) => (
                                            <div className={"WarehouseStateMenu-listItem"}
                                                 ref={provided.innerRef} {...provided.draggableProps} {...provided.dragHandleProps}>
                                                {item.driverName}
                                            </div>
                                        )}
                                    </Draggable>
                                ))}
                                {provided.placeholder}
                                .
                            </div>
                        )}
                    </Droppable>
                </DragDropContext>
            </div>
        </div>
    );
}

export default WarehouseStateMenu;