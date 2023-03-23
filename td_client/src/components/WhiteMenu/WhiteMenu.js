import './WhiteMenu.css';
import CheckInMenu from "../CheckInMenu/CheckOutMenu"
import WaitingAreaMenu from "../WaitingAreaMenu/WaitingAreaMenu"
import DockingAreaMenu from "../DockingAreaMenu/DockingAreaMenu"

function WhiteMenu({pageType}) {
  return ( 
    <div className="WhiteMenu">
        {pageType === "CheckIn" ? <CheckInMenu /> : pageType === "WaitingArea" ? <WaitingAreaMenu/> : <DockingAreaMenu/>}
    </div>
  );
}

export default WhiteMenu;