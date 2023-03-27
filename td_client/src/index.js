import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {WebSocketsProvider} from "./components/WebsocketContext/WebsocketContext";
import {AdminWebSocketsProvider} from "./components/WebsocketContext/AdminWebsocketContext";

//Render website
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <AdminWebSocketsProvider>
      <WebSocketsProvider>
        <App />
      </WebSocketsProvider>
  </AdminWebSocketsProvider>
);

reportWebVitals();
