import {createContext, useState} from "react";
import UseWebSocket from "react-use-websocket";
import {useCookies} from "react-cookie";
import {baseURL} from "./WebsocketContext";
export const AdminWebsocketContext = createContext(null);

const defaultURL = baseURL + 'admin';

const initialState = {
    "type": "",
    "truckID": "",
    "position": "",
    "username": "",
    "password": "",
    "sessionToken": ""
};

/**
 * The Websocket commands for sending Admin messages.
 * @param props Misc properties.
 * @return {JSX.Element} Context of websocket commands for sending Admin messages.
 */
export const AdminWebSocketsProvider = (props) => {
    const [receivedMessage, changeReceivedMessage] = useState(initialState);
    const [cookies, setCookies] = useCookies(['sessionToken']);

    //Open websocket for admin.
    const {sendJsonMessage} = UseWebSocket(defaultURL, {
        onOpen: () => console.log('Opened connection'),
        onError: () => console.log("Error"),
        onMessage: (event) => {
            const newMessage = JSON.parse(event.data);
            if (newMessage.type === "login") {
                setCookies("sessionToken", newMessage.sessionToken)
            }
            changeReceivedMessage(newMessage);
        },
        onClose: () => {
            console.log("Connection closed")
            changeReceivedMessage(initialState);
        },
        shouldReconnect: () => true,
        reconnectAttempts: 1000,
        reconnectInterval: 100,
    });

    const sendMessage = (message) => {
        message.sessionToken = cookies.sessionToken;
        sendJsonMessage(message);
    }

    return <AdminWebsocketContext.Provider value={{sendMessage, receivedMessage}} {...props} />
}