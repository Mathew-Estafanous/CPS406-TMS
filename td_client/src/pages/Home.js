import '../App.css';
import WhiteMenu from '../components/WhiteMenu/WhiteMenu';
import Logo from '../components/Logo/Logo';

/**
 * The Home/Check In page.
 * @return {JSX.Element} UI for the Home/Check In page.
 */
function Home() {
    return (
        <div className="App">
            <div className="Background-Image">
                <Logo/>
                <WhiteMenu pageType={"CheckIn"}/>
            </div>
        </div>
    );
}

export default Home;
