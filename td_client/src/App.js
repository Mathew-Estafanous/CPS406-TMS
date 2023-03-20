import './App.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import CheckOut from "./pages/CheckOut";
import React from 'react';

function App() {
    return (
    <BrowserRouter>
    <Routes>
      <Route exact path="/" element={<Home history/>}/>
      <Route exact path="/CheckOut" element={<CheckOut/>}/>
    </Routes>
    </BrowserRouter>
  );
}

export default App;
