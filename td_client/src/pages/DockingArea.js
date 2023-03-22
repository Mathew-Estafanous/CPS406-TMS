import '../App.css';
import WhiteMenu from '../components/WhiteMenu/WhiteMenu';
import Logo from '../components/Logo/Logo';

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
