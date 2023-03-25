import "./SelectBox.css"

function SelectBox({placeholder, name, changeHandler, selectType}) {
    if (selectType === "Admin") {
        return (
            <>
                <select className={"SelectBox-input"} name={name} onChange={changeHandler}>
                    <option value={"0"}>Get State</option>
                    <option value={"1"}>Reposition Truck</option>
                    <option value={"2"}>Cancel Truck</option>
                </select>
            </>
        );
    } else {
        return (
            <>
                <select className={"SelectBox-input"} name={name} onChange={changeHandler}>
                    <option value={"0"}>Default</option>
                </select>
            </>
        );
    }

}

export default SelectBox;