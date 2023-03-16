import './index.css';
import TextBox from '../TextBox'
import ClickBox from '../ClickBox';
import { useState } from "react";

function WhiteMenu() {

  const [inputs, setInputs] = useState({});
  const [errors, changeErrorMessage] = useState([]);

  const changeHandler = (event) => {
    setInputs(values => ({...values, [event.target.name]: event.target.value}))
  }

  const submitHandler = (event) => {
    event.preventDefault();

    let errorMessages = [...errors];

    //Logic for determining a valid Truck ID
    if (inputs.truckID == undefined || inputs.truckID == "") {
      errorMessages[0] = "Enter a truck ID";
    } 
    else if (!Number.isInteger(parseInt(inputs.truckID)) || parseInt(inputs.truckID) < 0) {
      errorMessages[0] = "Invalid truck ID";
    } else {
      errorMessages[0] = "";
    }

    if (inputs.estimatedTime == undefined || inputs.estimatedTime == "") {
      errorMessages[1] = "Enter an estimated time";
    } 
    else if (!Number.isInteger(parseInt(inputs.estimatedTime)) || parseInt(inputs.estimatedTime) < 0 || parseInt(inputs.estimatedTime) > 60) {
      errorMessages[1] = "Invalid estimation";
    } else {
      errorMessages[1] = "";
    }

    changeErrorMessage(errorMessages);



  }

  return (
    <div className="WhiteMenu">
      <h1 className="WhiteMenu-title">Check In</h1>
      <form onSubmit={submitHandler}>
      <TextBox placeholder={"Truck ID"} name={"truckID"} changeHandler={changeHandler}/>
      { errors[0] != "" && <label className='WhiteMenu-error'>{errors[0]}</label> }
      <TextBox placeholder={"Estimated Time"} name={"estimatedTime"} changeHandler={changeHandler}/>
      { errors[1] != "" && <label className='WhiteMenu-error'>{errors[1]}</label> }
      <ClickBox text={"Submit"}/>
      </form>
    </div>
  );
}

export default WhiteMenu;