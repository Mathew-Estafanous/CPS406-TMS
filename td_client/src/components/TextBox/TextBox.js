import './TextBox.css'

function TextBox({ placeholder, name, changeHandler, error}) {
  return (
    <div>
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
    </div>
  );
}

export default TextBox;