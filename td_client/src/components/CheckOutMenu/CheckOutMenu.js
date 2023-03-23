import ClickBox from "../ClickBox/ClickBox";
import '../WhiteMenu/WhiteMenu.css';
import { useNavigate } from "react-router-dom"

function CheckOut() {
  //Check out just takes you back to the Check In for now
  const navigate = useNavigate();

  const submitHandler = (_) => {
    navigate("/");
  }

  return (
    <div>
      <div className="WhiteMenu-header">
      Position Number: 5
      </div>
      <div className="WhiteMenu-header WhiteMenu-subheader">
      Estimated Time: 5
      </div>
      <form onSubmit={submitHandler}>
        <ClickBox text={"Cancel"}/>
      </form>
    </div>
  );
}

export default CheckOut;
