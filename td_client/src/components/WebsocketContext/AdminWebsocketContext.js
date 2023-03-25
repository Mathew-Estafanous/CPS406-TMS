import {createContext, useState} from "react";
import UseWebSocket from "react-use-websocket";
import {useCookies} from "react-cookie";

const defaultURL = 'ws://localhost:8080/admin';
export const AdminWebSocketContext = createContext(null);

const initialState = {
    "type": "",
    "truckID": "",
    "position": "",
    "username": "",
    "password": "",
    "sessionToken": ""
};

export const AdminWebSocketsProvider = (props) => {
    const [receivedMessage, changeReceivedMessage] = useState(initialState);
    const [_, setCookies ] = useCookies(['sessionToken']);

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
        reconnectAttempts: 10,
        reconnectInterval: 100,
    });

    return <AdminWebSocketContext.Provider value={{sendJsonMessage, receivedMessage}} {...props} />
}