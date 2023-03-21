import './ClickBox.css'

function ClickBox({text}) {
    return (
      <input 
        type="submit" 
        className="ClickBox-Input" 
        value={text}
      />
    );
  }
  
  export default ClickBox;