import {createContext, useState} from "react";
import UseWebSocket from "react-use-websocket";

export const baseURL = (process.env.NODE_ENV === 'production')? 'https://store-it-api.herokuapp.com/': 'http://localhost:8080/';

const defaultURL = baseURL + 'server/';
export const WebSocketContext = createContext(null);

const initialState = {
    "type": "",
    "truckID": "",
    "driverName": "",
    "estimatedTime": "PT0S",
    "locationState": "",
    "position": ""
};

/**
 * The Websocket commands for sending Truck messages.
 * @param props Misc properties.
 * @return {JSX.Element} Context of websocket commands for sending Truck messages.
 */
export const WebSocketsProvider = (props) => {
    const state = JSON.parse(sessionStorage.getItem("truckMessage")) || initialState;
    const [id, changeId] = useState(state.truckID);
    const [receivedMessage, changeReceivedMessage] = useState(state);

    //Open websocket for client.
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