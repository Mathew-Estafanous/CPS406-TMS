import './WhiteMenu.css';
import CheckInMenu from "../CheckInMenu/CheckOutMenu"
import CheckOutMenu from "../CheckOutMenu/CheckOutMenu"

function WhiteMenu({pageType}) {
  return ( //For now, white menu can only be a CheckIn page or a CheckOut page
    <div className="WhiteMenu">
        {pageType === "CheckIn" ? <CheckInMenu /> : <CheckOutMenu />}
    </div>
  );
}

export default WhiteMenu;