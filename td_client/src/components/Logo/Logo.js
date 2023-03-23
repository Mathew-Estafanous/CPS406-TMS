import "./Logo.css"
import BoxLogo from '../../BoxLogo.svg'

function Logo() {
  return (
    <div>
        <img alt="White Box Logo" src={BoxLogo} className="App-BoxLogo"></img>
        <h1 className="App-Title">warehouse</h1>
    </div>
  );
}

export default Logo;
