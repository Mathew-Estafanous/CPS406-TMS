import '../TextBox/TextBox.css'

function NumberBox({placeholder, name, changeHandler, error}) {
    return (
        <>
        { error ?
                <input
                    type="number"
                    className="NumberBox-input NumberBox-error"
                    autoComplete='off'

                    name={name}
                    placeholder={placeholder}
                    onChange={changeHandler}
                />
                :
                <input
                    type="number"
                    className="NumberBox-input"
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