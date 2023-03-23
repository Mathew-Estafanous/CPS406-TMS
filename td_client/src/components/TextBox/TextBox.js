import './TextBox.css'

function TextBox({ placeholder, name, changeHandler, error}) {
  return (
    <>
      {error ?
        <input
          type="text"
          className="TextBox-input TextBox-error"
          autoComplete='off'

          name={name}
          placeholder={placeholder}
          onChange={changeHandler}
        />
        :
        <input
          type="text"
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