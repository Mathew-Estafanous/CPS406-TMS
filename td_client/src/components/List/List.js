import "../WarehouseStateMenu/WarehouseStateMenu.css"
function List({list, title}) {
    return (
        <div className={"WarehouseStateMenu-listContainer"}>
            <div>
                <div className={"WarehouseStateMenu-header"}>{title}</div>
                    {list.map((item, _) => {
                        return (
                            <div key={item.truckID} className={"WarehouseStateMenu-listItem"}>
                                <div className={"textLeft"}>{item.driverName} </div>
                                <div className={"textRight"}>Truck ID: {item.truckID}</div>
                            </div>
                        );
                    })}
                <div className={"white"}>.</div>
            </div>
        </div>
    );
}

export default List;