import './ClickBox.css'

function ClickBox({text, clickHandle}) {
    return (
      <input 
        type="submit" 
        className="ClickBox-Input" 
        value={text}
        onClick={clickHandle}
      />
    );
  }
  
  export default ClickBox;