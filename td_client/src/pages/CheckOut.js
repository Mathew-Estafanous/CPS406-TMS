import '../App.css';
import WhiteMenu from '../components/WhiteMenu';
import Logo from '../components/Logo';

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
