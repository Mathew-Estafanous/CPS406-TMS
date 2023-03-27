import './App.css';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import Home from "./pages/Home";
import WaitingArea from "./pages/WaitingArea";
import DockingArea from "./pages/DockingArea";
import React from 'react';
import AdminLogin from "./pages/AdminLogin";
import AdminPortal from "./pages/AdminPortal";
import WarehouseState from "./pages/WarehouseState";
import PageNotFound from "./pages/PageNotFound";

/**
 * App determining the display for each page.
 * @return {JSX.Element}
 * @constructor
 */
function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route exact path="/" element={<Home history/>}/>
                <Route exact path="/WarehouseState" element={<WarehouseState/>}/>
                <Route exact path="/WaitingArea" element={<WaitingArea/>}/>
                <Route exact path="/DockingArea" element={<DockingArea/>}/>
                <Route exact path="/AdminLogin" element={<AdminLogin/>}/>
                <Route exact path="/AdminPortal" element={<AdminPortal/>}/>
                <Route path="*" element={<PageNotFound/>}/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
