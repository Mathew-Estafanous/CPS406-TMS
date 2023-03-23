import {createContext, useState} from "react";
import UseWebSocket from "react-use-websocket";

const defaultURL = 'ws://localhost:8080/server/';
export const WebSocketContext = createContext(null);

const initialState = {
    "type": "",
    "truckID": "",
    "driverName": "",
    "estimatedTime": "",
    "locationState": "",
    "position": ""
};

export const WebSocketsProvider = (props) => {
    const [id, changeId] = useState("");
    const [receivedMessage, changeReceivedMessage] = useState(initialState)
    const {sendJsonMessage} = UseWebSocket(defaultURL + id, {
        onOpen: () => console.log('Opened connection'),
        onError: () => console.log("Error"),
        onMessage: (event) => {
            console.log(event.data);
            changeReceivedMessage(JSON.parse(event.data));
        },
        onClose: () => console.log("Connection closed")
    });

    return <WebSocketContext.Provider value={{sendJsonMessage, id, changeId, receivedMessage}} {...props} />
}