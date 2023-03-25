import '../WhiteMenu/WhiteMenu.css';
import TextBox from '../TextBox/TextBox'
import ClickBox from '../ClickBox/ClickBox';
import {useContext, useState} from "react";
import {useNavigate} from "react-router-dom"
import {AdminWebSocketContext} from "../AdminWebsocketContext/AdminWebsocketContext";
import SelectBox from "../SelectBox/SelectBox";

function AdminPortalMenu() {
    const navigate = useNavigate();
    const {sendJsonMessage} = useContext(AdminWebSocketContext);
    const [adminCommand, changeAdminCommand] = useState("0")
    const [inputs, setInputs] = useState({"truckID": null, "position": null});
    const [errors, changeErrorMessage] = useState([false, false]);
    const changeHandler = (event) => {
        setInputs(values => ({...values, [event.target.name]: event.target.value}))
    }

    const exitAdminPortal = () => {
        navigate("/AdminLogin");
    }

    const handleCancelCommand = (errorMessages) => {
        //Cancel truck
        let adminMessage = {
            "type": "cancel",
            "truckID": inputs.truckID
        }

        if (inputs.truckID == null || inputs.truckID === "") {
            errorMessages[0] = "Enter a truck ID";
        } else if (!Number.isInteger(parseInt(inputs.truckID))) {
            errorMessages[0] = "Invalid truck ID";
        } else {
            errorMessages[0] = false;
        }

        changeErrorMessage(errorMessages);
        if (!errorMessages[0]) {
            //Send cancel
            sendJsonMessage(adminMessage)
            console.log("Cancel Message");
        }
    }

    const handleRepositionCommand = (errorMessages) => {
        //Reposition Truck
        let adminMessage = {
            "type": "change_position",
            "truckID": inputs.truckID,
            "position": inputs.newPosition
        };

        if (inputs.newPosition === null || inputs.newPosition === "") {
            errorMessages[1] = "Enter a position";
        } else if (!Number.isInteger(parseInt(inputs.newPosition)) || parseInt(inputs.newPosition) < 1) {
            errorMessages[1] = "Invalid position";
        } else {
            errorMessages[1] = false;
        }

        if (inputs.truckID === null || inputs.truckID === "") {
            errorMessages[0] = "Enter a truck ID";
        } else if (!Number.isInteger(parseInt(inputs.truckID))) {
            errorMessages[0] = "Invalid truck ID";
        } else {
            errorMessages[0] = false;
        }
        changeErrorMessage(errorMessages);
        //If no error message
        if (!errorMessages[0] && !errorMessages[1]) {
            //Send admin message
            sendJsonMessage(adminMessage)
            console.log("Reposition Message");
        }
    }

    const handleGetStateCommand = (errorMessages) => {
        let adminMessage = {
            "type": "view_state"
        }

    };


    const onChangedAdminCommand = (event) => {
        changeAdminCommand(event.target.value)
        //Reset inputs from other Admin commands
        if (adminCommand === "0"){
            setInputs({"truckID": null, "position": null});
            changeErrorMessage([false, false]);
        } else {
            setInputs({"truckID": inputs.truckID, "position": null});
            changeErrorMessage([errors[0], false]);
        }
    }
    const submitHandler = (event) => {
        event.preventDefault();
        let errorMessages = [...errors];
        console.log(inputs)
        if (adminCommand === "0") {
            handleGetStateCommand(errorMessages);
        } else if (adminCommand === "1") {
            handleRepositionCommand(errorMessages);
        } else {
            handleCancelCommand(errorMessages);
        }
    }

    return (
        <div>
            <div className="WhiteMenu-title">Admin Portal</div>
            <form onSubmit={submitHandler}>
                <SelectBox selectType={"Admin"} name={adminCommand} changeHandler={onChangedAdminCommand}/>
                {(() => {
                    switch (adminCommand) {
                        case "1":
                            return (<>
                                <TextBox placeholder={"Truck ID"} name={"truckID"} changeHandler={changeHandler} error={errors[0]}/>
                                <label className={errors[0] ? 'WhiteMenu-error' : 'WhiteMenu-error WhiteMenu-hide'}>{errors[0] ? errors[0] : "."}</label>
                                <TextBox placeholder={"Position Number"} name={"newPosition"} changeHandler={changeHandler} error={errors[1]}/>
                                <label className={errors[1] ? 'WhiteMenu-error' : 'WhiteMenu-error WhiteMenu-hide'}>{errors[1] ? errors[1] : "."}</label>
                            </>)
                        case "2":
                            return (<>
                                <TextBox placeholder={"Truck ID"} name={"truckID"} changeHandler={changeHandler} error={errors[0]}/>
                                <label className={errors[0] ? 'WhiteMenu-error' : 'WhiteMenu-error WhiteMenu-hide'}>{errors[0] ? errors[0] : "."}</label>
                            </>)
                        default:
                            return <></>
                    }
                })()}
                <ClickBox text={"Submit"}/>
            </form>
            <ClickBox text={"Exit Portal"} clickHandle={exitAdminPortal}/>
        </div>
    );
}

export default AdminPortalMenu;