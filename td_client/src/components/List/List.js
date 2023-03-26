import "../WarehouseStateMenu/WarehouseStateMenu.css"
function List({list, title, cancelCommand}) {
    return (
        <div className={"WarehouseStateMenu-listContainer"}>
            <div>
                <div className={"WarehouseStateMenu-header"}>{title}</div>
                <div className={"Divider Divider-spaced Divider-red"}/>
                {sortedList.map((item, _) => {
                        return (
                            <div key={item.truckID} className={"WarehouseStateMenu-listItem"}>
                                <div className={"textLeft"}>{item.driverName} </div>
                                <div className={"textRight"}>Truck ID: {item.truckID}</div>
                                <div className={"textCancel textCancel-red"} onClick={() => cancelCommand(item.truckID)}>X</div>
                            </div>
                        );
                    })}
            </div>
        </div>
    );
}

export default List;