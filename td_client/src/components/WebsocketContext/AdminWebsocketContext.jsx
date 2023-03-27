import {createContext, useState} from "react";
import UseWebSocket from "react-use-websocket";
import {useCookies} from "react-cookie";
const defaultURL = 'ws://localhost:8080/admin';
export const AdminWebsocketContext = createContext(null);

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
    const [_, setCookies] = useCookies(['sessionToken']);

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

    return <AdminWebsocketContext.Provider value={{sendJsonMessage, receivedMessage}} {...props} />
}