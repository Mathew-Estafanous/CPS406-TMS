import '../App.css';
import WhiteMenu from '../components/WhiteMenu/WhiteMenu';
import Logo from '../components/Logo/Logo';

//className="Background-gradient"
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
