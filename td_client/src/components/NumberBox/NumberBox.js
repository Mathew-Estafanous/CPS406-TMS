import './NumberBox.css'

function NumberBox({placeholder, name, changeHandler}) {
    return (
      <input 
      type="number" 
      className="NumberBox-input" 
      autoComplete='off'

      name={name}
      placeholder={placeholder}
      onChange={changeHandler}
      />
    );
  }
  
  export default NumberBox;