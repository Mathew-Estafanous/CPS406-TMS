import './App.css';
import BoxLogo from './BoxLogo.svg'
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import CheckIn from "./pages/CheckIn";

function App() {
  return (
    <BrowserRouter>
    <Routes>
      <Route exact path="/" element={<Home history/>}/>
      <Route exact path="/CheckIn" element={<CheckIn/>}/>
    </Routes>
    </BrowserRouter>
  );
}

export default App;
