import '../WhiteMenu/WhiteMenu.css';
import TextBox from '../TextBox/TextBox'
import ClickBox from '../ClickBox/ClickBox';
import NumberBox from '../NumberBox/NumberBox';
import {useContext, useState} from "react";
import { useNavigate } from "react-router-dom"
import {WebSocketContext} from "../WebsocketContext/WebsocketContext";

function timeToString(time) {
  const hours = Math.floor(time/60);
  const minutes = time%60;
  return "PT"+hours+"H"+minutes+"M0S";
}

function CheckIn() {
  const navigate = useNavigate();
  const [inputs, setInputs] = useState({});
  const [errors, changeErrorMessage] = useState([".",".","."]);
  const {sendJsonMessage, changeId} = useContext(WebSocketContext);

  const changeHandler = (event) => {
    setInputs(values => ({...values, [event.target.name]: event.target.value}))
  }

  const submitHandler = (event) => {
    event.preventDefault();
    let errorMessages = [...errors];
    let message = {
      "type": "check-in",
      "truckID": "",
      "driverName": "",
      "estimatedTime": ""
    }
    let estimatedTimeString = ""

    //Logic for determining a valid name
    if (inputs.driverName === undefined || inputs.driverName === "") {
      errorMessages[0] = "Enter a name";
    } else {
      errorMessages[0] = ".";
    }

    //Logic for determining a valid Truck ID
    if (inputs.truckID === undefined || inputs.truckID === "") {
      errorMessages[1] = "Enter a truck ID";
    } 
    else if (!Number.isInteger(parseInt(inputs.truckID)) || parseInt(inputs.truckID) < 0) {
      errorMessages[1] = "Invalid truck ID";
    } else {
      errorMessages[1] = ".";
    }

    //Logic for determining a valid estimated time
    if (inputs.estimatedTime === undefined || inputs.estimatedTime === "") {
      errorMessages[2] = "Enter an estimated time";
    } 
    else if (!Number.isInteger(parseInt(inputs.estimatedTime)) || parseInt(inputs.estimatedTime) < 0 || parseInt(inputs.estimatedTime) > 360) {
      errorMessages[1] = "Invalid estimation";
    } else {
      errorMessages[2] = ".";
    }

    changeErrorMessage(errorMessages);

    if (errorMessages[0] === "." && errorMessages[1] === "." && errorMessages[2] === ".") {
      estimatedTimeString = timeToString(inputs.estimatedTime);

      message.driverName = inputs.driverName;
      message.truckID = inputs.truckID;
      message.estimatedTime = estimatedTimeString;
      console.log(message);
      changeId(message.truckID);
      sendJsonMessage(message);
      navigate("/WaitingArea");
    }


  }

  return (
    <div>
      <div className="WhiteMenu-title">Check In</div>
      <form onSubmit={submitHandler}>
      { errors[0] !== "." ? <TextBox placeholder={"Name"} name={"driverName"} changeHandler={changeHandler} error={true}/> :  <TextBox placeholder={"Name"} name={"driverName"} changeHandler={changeHandler} error={false}/>}
      { errors[0] !== "." ? <label className='WhiteMenu-error'>{errors[0]}</label> : <label className='WhiteMenu-error WhiteMenu-hide'>.</label>}      
      { errors[0] !== "." ? <TextBox placeholder={"Truck ID"} name={"truckID"} changeHandler={changeHandler} error={true}/> :  <TextBox placeholder={"Truck ID"} name={"truckID"} changeHandler={changeHandler} error={false}/>}
      { errors[1] !== "." ? <label className='WhiteMenu-error'>{errors[1]}</label> : <label className='WhiteMenu-error WhiteMenu-hide'>.</label>}      
      <NumberBox placeholder={"Estimated Minutes"} name={"estimatedTime"} changeHandler={changeHandler}/>
      { errors[2] !== "." ? <label className='WhiteMenu-error'>{errors[2]}</label> : <label className='WhiteMenu-error WhiteMenu-hide'>.</label>}      
      <ClickBox text={"Submit"}/>
      </form>
    </div>
  );
}

export default CheckIn;