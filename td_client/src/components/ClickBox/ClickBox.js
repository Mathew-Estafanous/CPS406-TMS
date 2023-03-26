import './ClickBox.css'

function ClickBox({text, clickHandle, color}) {
    return (
      <input 
        type="submit" 
        className={color ? "ClickBox-Input"+" ClickBox-"+color : "ClickBox-Input"}
        value={text}
        onClick={clickHandle}
      />
    );
  }
  
  export default ClickBox;