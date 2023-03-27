import "../WarehouseStateMenu/WarehouseStateMenu.css"

/**
 *
 * @param {Array} list List consisting of Truck Drivers.
 * @param {String} title Name of the list.
 * @param {Function} cancelCommand Function used to cancel Truck Drivers in the list.
 * @return {JSX.Element} A UI list.
 */
function List({list, title, cancelCommand}) {
    //Sort the list in increasing order of Truck positions
    let sortedList = [...list].sort((a, b) => a.position - b.position);

    return (
        <div className={"WarehouseStateMenu-listContainer"}>
            <div>
                <div className={"WarehouseStateMenu-header"}>{title}</div>
                <div className={"Divider Divider-spaced Divider-red"}/>
                {sortedList.map((item, _) => {
                    return (
                        <div key={item.truckID} className={"WarehouseStateMenu-listItem listItem-red"}>
                            <div className={"textLeft"}>{item.driverName} </div>
                            <div className={"textRight"}>Truck ID: {item.truckID}</div>
                            <div className={"textCancel textCancel-red"} onClick={() => cancelCommand(item.truckID)}>X
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}

export default List;