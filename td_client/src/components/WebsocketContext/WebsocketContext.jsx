import {createContext, useState} from "react";
import UseWebSocket from "react-use-websocket";

const defaultURL = 'ws://localhost:8080/server/';
export const WebSocketContext = createContext(null);

const initialState = {
    "type": "",
    "truckID": "",
    "driverName": "",
    "estimatedTime": "PT0S",
    "locationState": "",
    "position": ""
};

export const WebSocketsProvider = (props) => {
    const state = JSON.parse(sessionStorage.getItem("truckMessage")) || initialState;
    const [id, changeId] = useState(state.truckID);
    const [receivedMessage, changeReceivedMessage] = useState(state);
    const {sendJsonMessage} = UseWebSocket(defaultURL + id, {
        onOpen: () => console.log('Opened connection'),
        onError: () => console.log("Error"),
        onMessage: (event) => {
            const newMessage = JSON.parse(event.data);
            changeReceivedMessage(newMessage);
            sessionStorage.setItem("truckMessage", JSON.stringify(newMessage))
        },
        onClose: () => {
            changeReceivedMessage(initialState);
            changeId("");
        }
    });

    return <WebSocketContext.Provider value={{sendJsonMessage, id, changeId, receivedMessage}} {...props} />
}