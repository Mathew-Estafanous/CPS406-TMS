import "./Logo.css"
import BoxLogo from '../../BoxLogo.svg'

/**
 * The Store.it logo.
 * @return {JSX.Element} The logo with the project name.
 */
function Logo() {
    return (
        <div>
            <img alt="White Box Logo" src={BoxLogo} className="App-BoxLogo"></img>
            <h1 className="App-Title">Store.it</h1>
        </div>
    );
}

export default Logo;
