import ClickBox from "../ClickBox";
import { useNavigate } from "react-router-dom"

function CheckOut() {
  //Check out just takes you back to the Check In for now
  const navigate = useNavigate();

  const submitHandler = (_) => {
    navigate("/");
  }

  return (
    <div>
      This is the check out
      <form onSubmit={submitHandler}>
        <ClickBox text={"Cancel"}/>
      </form>
    </div>
  );
}

export default CheckOut;
