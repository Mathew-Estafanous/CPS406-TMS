import '../App.css';
import WhiteMenu from '../components/WhiteMenu/WhiteMenu';
import Logo from '../components/Logo/Logo';

/**
 * The Waiting Area page.
 * @return {JSX.Element} UI for the Waiting Area page.
 */
function WaitingArea() {
    return (
        <div className="App">
            <div className="Background-Image">
                <Logo/>
                <WhiteMenu pageType={"WaitingArea"}/>
            </div>
        </div>
    );
}

export default WaitingArea;
