import './index.css';
import CheckIn from "../CheckIn"
import CheckOut from "../CheckOut"
function WhiteMenu({pageType}) {

  return ( //For now, white menu can only be a CheckIn page or a CheckOut page
    <div className="WhiteMenu">
        {pageType === "CheckIn" ? <CheckIn /> : <CheckOut />}
    </div>
  );
}

export default WhiteMenu;