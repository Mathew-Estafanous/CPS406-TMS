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
 * CheckIn represents the menu UI for the Check-in page.
 * @return {JSX.Element} The UI for the Check-in menu.
 */
function CheckIn() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const [inputs, setInputs] = useState({});
    const [errors, changeErrorMessage] = useState([".", ".", "."]);
    const {sendJsonMessage, changeId, receivedMessage} = useContext(WebSocketContext);


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

    const toAdminLogin = () => {
        navigate("/AdminLogin");
    }

    const changeHandler = (event) => {
        setInputs(values => ({...values, [event.target.name]: event.target.value}))
    }

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
            errorMessages[0] = ".";
        }

        //Logic for determining a valid Truck ID
        if (inputs.truckID === undefined || inputs.truckID === "") {
            errorMessages[1] = "Enter a truck ID";
        } else if (!Number.isInteger(parseInt(inputs.truckID)) || parseInt(inputs.truckID) < 0) {
            errorMessages[1] = "Invalid truck ID";
        } else {
            errorMessages[1] = ".";
        }

        //Logic for determining a valid estimated time
        if (inputs.estimatedTime === undefined || inputs.estimatedTime === "") {
            errorMessages[2] = "Enter an estimated time";
        } else if (!Number.isInteger(parseInt(inputs.estimatedTime)) || parseInt(inputs.estimatedTime) < 1) {
            errorMessages[2] = "Invalid estimation";
        } else if (parseInt(inputs.estimatedTime) > 360) {
            errorMessages[2] = "Maximum 360 minutes"
        } else {
            errorMessages[2] = ".";
        }

        changeErrorMessage(errorMessages);

        if (errorMessages[0] === "." && errorMessages[1] === "." && errorMessages[2] === ".") {
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
                {errors[0] !== "." ?
                    <TextBox placeholder={"Name"} name={"driverName"} changeHandler={changeHandler} error={true}/> :
                    <TextBox placeholder={"Name"} name={"driverName"} changeHandler={changeHandler} error={false}/>}
                {errors[0] !== "." ? <label className='WhiteMenu-error'>{errors[0]}</label> :
                    <label className='WhiteMenu-error WhiteMenu-hide'>.</label>}
                {errors[1] !== "." ?
                    <NumberBox placeholder={"Truck ID"} name={"truckID"} changeHandler={changeHandler} error={true}/> :
                    <NumberBox placeholder={"Truck ID"} name={"truckID"} changeHandler={changeHandler} error={false}/>}
                {errors[1] !== "." ? <label className='WhiteMenu-error'>{errors[1]}</label> :
                    <label className='WhiteMenu-error WhiteMenu-hide'>.</label>}
                {errors[2] !== "." ?
                    <NumberBox placeholder={"Estimated Minutes"} name={"estimatedTime"} changeHandler={changeHandler}
                               error={true}/> :
                    <NumberBox placeholder={"Estimated Minutes"} name={"estimatedTime"} changeHandler={changeHandler}
                               error={false}/>}
                {errors[2] !== "." ? <label className='WhiteMenu-error'>{errors[2]}</label> :
                    <label className='WhiteMenu-error WhiteMenu-hide'>.</label>}
                <ClickBox text={"Submit"} color={"green"}/>
            </form>
            <ClickBox text={"Admin Login"} clickHandle={toAdminLogin}/>
        </div>
    );
}

export default CheckIn;