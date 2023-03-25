import {DragDropContext, Draggable, Droppable} from "react-beautiful-dnd";
import "../WarehouseStateMenu/WarehouseStateMenu.css"

function DraggableList({list, title, warehouseState}){
    return(
        <div className={"WarehouseStateMenu-listContainer"}>
            <DragDropContext onDragEnd={(dragItem) => {
                const sourceIndex = dragItem.source.index;
                const destIndex = dragItem.destination.index;
                list.splice(destIndex, 0, list.splice(sourceIndex, 1)[0]);
                console.log(warehouseState);
                localStorage.setItem("warehouseState", JSON.stringify(warehouseState));
            }}>
                <Droppable droppableId={"droppable-1"}>
                    {(provided, _) => (
                        <div ref={provided.innerRef} {...provided.droppableProps}>
                            <div className={"WarehouseStateMenu-header"}>{title}</div>
                            {list.map((item, i) => (
                                <Draggable key={item.id} draggableId={"draggable-" + item.id} index={i}>
                                    {(provided, _) => (
                                        <div className={"WarehouseStateMenu-listItem"}
                                             ref={provided.innerRef} {...provided.draggableProps} {...provided.dragHandleProps}>
                                            {item.driverName}
                                            {item.id}
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
    );
}

export default DraggableList;