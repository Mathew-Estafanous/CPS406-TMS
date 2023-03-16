import './index.css'

function TextBox({placeholder, name, changeHandler}) {
  return (
    <input 
      type="text" 
      className="TextBox-input" 
      autoComplete='off'

      name={name}
      placeholder={placeholder}
      onChange={changeHandler}
      />
  );
}

export default TextBox;