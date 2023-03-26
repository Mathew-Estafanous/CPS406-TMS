import '../TextBox/TextBox.css'

function NumberBox({placeholder, name, changeHandler, error}) {
    return (
        <>
        { error ?
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