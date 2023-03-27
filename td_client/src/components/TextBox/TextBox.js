import './TextBox.css'

/**
 * Input UI for a text field.
 * @param {String} placeholder Placeholder text.
 * @param {String} name Name of text box.
 * @param {Function} changeHandler Function to update state of input.
 * @param {Boolean} error If an error has been detected from input.
 * @param {Boolean} password If the text box should be treated like a password.
 * @return {JSX.Element} UI for a text box.
 * @constructor
 */
function TextBox({placeholder, name, changeHandler, error, password}) {
    return (
        <>
            {error ?
                <input
                    type={password ? "password" : "text"}
                    className="TextBox-input TextBox-error"
                    autoComplete='off'

                    name={name}
                    placeholder={placeholder}
                    onChange={changeHandler}
                />
                :
                <input
                    type={password ? "password" : "text"}
                    className="TextBox-input"
                    autoComplete='off'

                    name={name}
                    placeholder={placeholder}
                    onChange={changeHandler}
                />

            }
        </>
    );
}

export default TextBox;