import '../TextBox/TextBox.css'

/**
 *
 * @param {String} placeholder Placeholder text.
 * @param {String} name Name of the number box.
 * @param {Function} changeHandler Function used to change the state of the inputs.
 * @param {Boolean} error If an error is detected in the inputs.
 * @return {JSX.Element} A numeric input box.
 * @constructor
 */
function NumberBox({placeholder, name, changeHandler, error}) {
    return (
        <>
            {error ?
                <input
                    type="number"
                    className="TextBox-input TextBox-error removeArrows"
                    autoComplete='off'

                    name={name}
                    placeholder={placeholder}
                    onChange={changeHandler}
                />
                :
                <input
                    type="number"
                    className="TextBox-input removeArrows"
                    autoComplete='off'

                    name={name}
                    placeholder={placeholder}
                    onChange={changeHandler}
                />
            }
        </>
    );
}

export default NumberBox;