import ClickBox from "../ClickBox/ClickBox";
import '../WhiteMenu/WhiteMenu.css';
import { useNavigate } from "react-router-dom"

function DockingAreaMenu() {
  //Check out just takes you back to the Check In for now
  const navigate = useNavigate();

  const submitHandler = (_) => {
    navigate("/");
  }

  return (
    <div>
      <div className="WhiteMenu-header">
      Docking Area: 4
      </div>
      <div className="WhiteMenu-header WhiteMenu-subheader">
      Estimated Time: 66
      </div>
      <form onSubmit={submitHandler}>
        <ClickBox text={"Check Out"}/>
      </form>
    </div>
  );
}

export default DockingAreaMenu;
