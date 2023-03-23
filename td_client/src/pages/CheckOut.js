import '../App.css';
import WhiteMenu from '../components/WhiteMenu/WhiteMenu';
import Logo from '../components/Logo/Logo';

function CheckOut() {
  return (
    <div className="App">
      <div className="Background-Image">
        <Logo/>
        <WhiteMenu pageType={"CheckOut"}/>
        </div>
    </div>
  );
}

export default CheckOut;
