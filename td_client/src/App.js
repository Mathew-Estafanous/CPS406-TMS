import './App.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import WaitingArea from "./pages/WaitingArea";
import DockingArea from "./pages/DockingArea";
import React from 'react';

function App() {
    return (
    <BrowserRouter>
        <Routes>
          <Route exact path="/" element={<Home history/>}/>
          <Route exact path="/WaitingArea" element={<WaitingArea/>}/>
          <Route exact path="/DockingArea" element={<DockingArea/>}/>
        </Routes>
    </BrowserRouter>
  );
}

export default App;
