import {createContext, useState} from "react";
import UseWebSocket from "react-use-websocket";

const defaultURL = 'ws://localhost:8080/admin';
export const AdminWebSocketContext = createContext(null);

const initialState = {
    "type": "",
    "truckID": "",
    "driverName": "",
    "estimatedTime": "PT0S",
    "locationState": "",
    "position": ""
};

export const AdminWebSocketsProvider = (props) => {
    const state = JSON.parse(sessionStorage.getItem("message")) || initialState;
    const [receivedMessage, changeReceivedMessage] = useState(state);
    const {sendJsonMessage} = UseWebSocket(defaultURL, {
        onOpen: () => console.log('Opened connection'),
        onError: () => console.log("Error"),
        onMessage: (event) => {
            const newMessage = JSON.parse(event.data);
            changeReceivedMessage(newMessage);
            sessionStorage.setItem("message", JSON.stringify(newMessage))
        },
        onClose: () => {
            changeReceivedMessage(initialState);
        }
    });

    return <AdminWebSocketContext.Provider value={{sendJsonMessage, receivedMessage}} {...props} />
}