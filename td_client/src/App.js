import './App.css';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import Home from "./pages/Home";
import WaitingArea from "./pages/WaitingArea";
import DockingArea from "./pages/DockingArea";
import React from 'react';
import AdminLogin from "./pages/AdminLogin";
import AdminPortal from "./pages/AdminPortal";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route exact path="/WaitingArea" element={<WaitingArea/>}/>
                <Route exact path="/DockingArea" element={<DockingArea/>}/>
                <Route exact path="/AdminLogin" element={<AdminLogin/>}/>
                <Route exact path="/AdminPortal" element={<AdminPortal/>}/>
                <Route exact path="/" element={<Home history/>}/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
