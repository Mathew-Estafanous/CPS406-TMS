import '../App.css';
import WhiteMenu from '../components/WhiteMenu/WhiteMenu';
import Logo from '../components/Logo/Logo';

function AdminPortal() {
    return (
        <div className="App">
            <div className="Background-Image">
                <Logo/>
                <WhiteMenu pageType={"AdminPortal"}/>
            </div>
        </div>
    );
}

export default AdminPortal;
