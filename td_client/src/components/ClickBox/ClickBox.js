import './ClickBox.css'

/**
 * ClickBox represents the UI for a button.
 * @param {String} text Text to be displayed in the button.
 * @param {Function} clickHandle Function to be called when clicking the button.
 * @param {String} color Color of the button.
 * @return {JSX.Element} A button.
 */
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