import '../App.css';
import WhiteMenu from '../components/WhiteMenu/WhiteMenu';
import Logo from '../components/Logo/Logo';

/**
 * The Docking Area page.
 * @return {JSX.Element} UI for the Docking Area page.
 */
function DockingArea() {
    return (
        <div className="App">
            <div className="Background-Image">
                <Logo/>
                <WhiteMenu pageType={"DockingArea"}/>
            </div>
        </div>
    );
}

export default DockingArea;
