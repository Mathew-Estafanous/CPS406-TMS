import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {WebSocketsProvider} from "./components/WebsocketContext/WebsocketContext";
import {AdminWebSocketsProvider} from "./components/WebsocketContext/AdminWebsocketContext";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <AdminWebSocketsProvider>
      <WebSocketsProvider>
        <App />
      </WebSocketsProvider>
  </AdminWebSocketsProvider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
