import {DragDropContext, Draggable, Droppable} from "react-beautiful-dnd";
import "../WarehouseStateMenu/WarehouseStateMenu.css"

function DraggableList({list, title, sendRepositionCommand, cancelCommand}){
    return(
        <div className={"WarehouseStateMenu-listContainer"}>
            <DragDropContext onDragEnd={(dragItem) => {
                const sourceIndex = dragItem.source.index;
                const truckID = dragItem.draggableId.slice(10);
                const newPosition = dragItem.destination.index+1;
                sendRepositionCommand(truckID, newPosition, sourceIndex);
            }}>
                <Droppable droppableId={"droppable-1"}>
                    {(provided, _) => (
                        <div ref={provided.innerRef} {...provided.droppableProps}>
                            <div className={"WarehouseStateMenu-header"}>{title}</div>
                            <div className={"Divider Divider-spaced Divider-blue"}/>
                            {list.map((item, i) => (
                                <Draggable key={item.truckID} draggableId={"draggable-" + item.truckID} index={i}>
                                    {(provided, snapshot) => (
                                        <div className={"WarehouseStateMenu-listItem listItem-blue"}
                                             ref={provided.innerRef} {...provided.draggableProps} {...provided.dragHandleProps}
                                             style={{
                                                 ...provided.draggableProps.style,
                                                 boxShadow: snapshot.isDragging ? "0 0 .4rem #3185FC" : "none",
                                                 "border-color": snapshot.isDragging && "#3185FC",
                                                 color: snapshot.isDragging && "white",
                                             }}
                                        >
                                            <div className={"textLeft"}>{item.driverName} </div>
                                            <div className={"textRight"}>Truck ID: {item.truckID}</div>
                                            <div className={"textCancel textCancel-blue"} onClick={() => cancelCommand(item.truckID)}>X</div>
                                        </div>
                                    )}
                                </Draggable>
                            ))}
                        </div>
                    )}
                </Droppable>
            </DragDropContext>
        </div>
    );
}

export default DraggableList;