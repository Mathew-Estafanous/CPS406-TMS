import '../WhiteMenu/WhiteMenu.css';
import TextBox from '../TextBox/TextBox'
import ClickBox from '../ClickBox/ClickBox';
import {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom"
import {AdminWebsocketContext} from "../WebsocketContext/AdminWebsocketContext";

/**
 * AdminLoginMenu represents the menu UI for the Admin Login.
 * @returns {JSX.Element} The UI for the Admin Login.
 */
function AdminLoginMenu() {
    const navigate = useNavigate();
    const [inputs, setInputs] = useState({"username": null, "password": null});
    const [errors, changeErrorMessage] = useState([false, false]);
    const {sendJsonMessage, receivedMessage} = useContext(AdminWebsocketContext);

    useEffect(() => {
        if (receivedMessage.type === "login") {
            navigate("/WarehouseState");
        } else if (receivedMessage.type === "failed") {
            changeErrorMessage(["Incorrect username or password", false]);
        }
    }, [receivedMessage]);

    const toTruckLogin = () => {
        navigate("/");
    }
    const changeHandler = (event) => {
        setInputs(values => ({...values, [event.target.name]: event.target.value}))
    }

    const submitHandler = (event) => {
        event.preventDefault();
        let errorMessages = [...errors];
        if (inputs.username === null || inputs.username === "") {
            errorMessages[0] = "Enter a username";
        } else {
            errorMessages[0] = false;
        }

        if (inputs.password === null || inputs.password === "") {
            errorMessages[1] = "Enter a password";
        } else {
            errorMessages[1] = false;
        }
        changeErrorMessage(errorMessages);

        if (!errorMessages[1] && !errorMessages[0]) {
            let loginMessage = {
                "type": "login",
                "username": inputs.username,
                "password": inputs.password
            }
            sendJsonMessage(loginMessage);
        }
    }

    return (
        <div>
            <div className="WhiteMenu-title">Admin Login</div>
            <form onSubmit={submitHandler}>
                <TextBox changeHandler={changeHandler} name={"username"} placeholder={"Username"} error={errors[0]}/>
                <label
                    className={errors[0] ? 'WhiteMenu-error' : 'WhiteMenu-error WhiteMenu-hide'}>{errors[0] ? errors[0] : "."}</label>
                <TextBox changeHandler={changeHandler} name={"password"} placeholder={"Password"} error={errors[1]}
                         password={true}/>
                <label
                    className={errors[1] ? 'WhiteMenu-error' : 'WhiteMenu-error WhiteMenu-hide'}>{errors[1] ? errors[1] : "."}</label>
                <ClickBox text={"Submit"} color={"green"}/>
            </form>
            <ClickBox text={"Truck Login"} clickHandle={toTruckLogin}/>
        </div>
    );
}

export default AdminLoginMenu;