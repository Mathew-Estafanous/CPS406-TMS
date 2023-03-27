import '../App.css';
import WhiteMenu from '../components/WhiteMenu/WhiteMenu';
import Logo from '../components/Logo/Logo';

/**
 * The Admin Login page.
 * @return {JSX.Element} UI for the Admin Login page.
 */
function AdminLogin() {
    return (
        <div className="App">
            <div className="Background-Image">
                <Logo/>
                <WhiteMenu pageType={"AdminLogin"}/>
            </div>
        </div>
    );
}

export default AdminLogin;
