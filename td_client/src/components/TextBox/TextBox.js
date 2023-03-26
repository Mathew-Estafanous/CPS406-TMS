import './TextBox.css'

function TextBox({ placeholder, name, changeHandler, error, password}) {
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