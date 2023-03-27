import '../WhiteMenu/WhiteMenu.css';
import TextBox from '../TextBox/TextBox'
import ClickBox from '../ClickBox/ClickBox';
import NumberBox from '../NumberBox/NumberBox';
import {useContext, useEffect, useState} from "react";
import {useNavigate, useSearchParams} from "react-router-dom"
import {WebSocketContext} from "../WebsocketContext/WebsocketContext";
import {serialize} from "tinyduration";
import {toast, ToastContainer} from "react-toastify";

/**
 * The menu UI for the Check-in page.
 * @return {JSX.Element} The UI for the Check-in menu.
 */
function CheckIn() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const [inputs, setInputs] = useState({});
    const [errors, changeErrorMessage] = useState([false, false, false]);
    const {sendJsonMessage, changeId, receivedMessage} = useContext(WebSocketContext);

    //Handler for notifying if the Admin kicked out the client.
    useEffect(() => {
        if (searchParams.get("wasKicked") === "true") {
            toast.error('Admin has checked you out of the warehouse', {
                toastId: 2,
                position: "top-left",
                autoClose: 1500,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "colored",
            });
        }
    }, [searchParams]);

    //Navigate to Admin Login page
    const toAdminLogin = () => {
        navigate("/AdminLogin");
    }

    //Handles inputs from text boxes.
    const changeHandler = (event) => {
        setInputs(values => ({...values, [event.target.name]: event.target.value}))
    }

    //Handles server response to checking in.
    useEffect(() => {
        console.log("CHECK IN:");
        console.log(receivedMessage);
        if (receivedMessage.type !== "check-in") return;
        if (receivedMessage.locationState === "waiting_area") {
            navigate("/WaitingArea");
        } else if (receivedMessage.locationState === "docking_area") {
            navigate("/DockingArea");
        } else if (receivedMessage.locationState === "unknown") {
            let errorMessages = [...errors];
            errorMessages[1] = "Existing Truck ID";
            changeErrorMessage(errorMessages);
        }
    }, [receivedMessage])

    //Handles submission to checking in.
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
        } else if (inputs.driverName.length > 20) {
            errorMessages[0] = "Maximum 20 characters";
        } else {
            errorMessages[0] = false;
        }

        //Logic for determining a valid Truck ID
        if (inputs.truckID === undefined || inputs.truckID === "") {
            errorMessages[1] = "Enter a truck ID";
        } else if (!Number.isInteger(parseInt(inputs.truckID)) || parseInt(inputs.truckID) < 0) {
            errorMessages[1] = "Invalid truck ID";
        } else {
            errorMessages[1] = false;
        }

        //Logic for determining a valid estimated time
        if (inputs.estimatedTime === undefined || inputs.estimatedTime === "") {
            errorMessages[2] = "Enter an estimated time";
        } else if (!Number.isInteger(parseInt(inputs.estimatedTime)) || parseInt(inputs.estimatedTime) < 1) {
            errorMessages[2] = "Invalid estimation";
        } else if (parseInt(inputs.estimatedTime) > 360) {
            errorMessages[2] = "Maximum 360 minutes"
        } else {
            errorMessages[2] = false;
        }

        changeErrorMessage(errorMessages);

        //Send message to server if no error messages.`
        if (!errorMessages[0] && !errorMessages[1] && !errorMessages[2]) {
            estimatedTimeString = serialize({minutes: inputs.estimatedTime});
            message.driverName = inputs.driverName;
            message.truckID = inputs.truckID;
            message.estimatedTime = estimatedTimeString;

            console.log(message);
            changeId(message.truckID);
            sendJsonMessage(message);
        }
    }

    return (
        <div>
            <ToastContainer limit={1}/>
            <div className="WhiteMenu-title">Check In</div>
            <form onSubmit={submitHandler}>
                <TextBox placeholder={"Name"} name={"driverName"} changeHandler={changeHandler} error={errors[0]}/>
                <label
                    className={errors[0] ? 'WhiteMenu-error' : 'WhiteMenu-error WhiteMenu-hide'}>{errors[0] ? errors[0] : "."}</label>
                <NumberBox placeholder={"Truck ID"} name={"truckID"} changeHandler={changeHandler} error={errors[1]}/>
                <label
                    className={errors[1] ? 'WhiteMenu-error' : 'WhiteMenu-error WhiteMenu-hide'}>{errors[1] ? errors[1] : "."}</label>
                <NumberBox placeholder={"Estimated Minutes"} name={"estimatedTime"} changeHandler={changeHandler}
                           error={errors[2]}/>
                <label
                    className={errors[2] ? 'WhiteMenu-error' : 'WhiteMenu-error WhiteMenu-hide'}>{errors[2] ? errors[2] : "."}</label>
                <ClickBox text={"Submit"} color={"green"}/>
            </form>
            <ClickBox text={"Admin Login"} clickHandle={toAdminLogin}/>
        </div>
    );
}

export default CheckIn;