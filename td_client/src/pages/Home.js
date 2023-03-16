import '../App.css';
import BoxLogo from '../BoxLogo.svg'
import WhiteMenu from '../components/WhiteMenu';

function Home() {
  return (
    <div className="App">
        <img src={BoxLogo} className="App-BoxLogo"></img>
        <h1 className="App-Title">warehouse</h1>
        <WhiteMenu/>
    </div>
  );
}

export default Home;
