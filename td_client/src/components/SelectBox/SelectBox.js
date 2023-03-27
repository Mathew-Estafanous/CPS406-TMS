import "./SelectBox.css"

/**
 * UI for a selection box.
 * @param {String} name Name of selection box.
 * @param {Function} changeHandler Function used to update state of selection box.
 * @param {String} selectType Type of selection box.
 * @return {JSX.Element} UI for a selection box.
 */
function SelectBox({name, changeHandler, selectType}) {
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