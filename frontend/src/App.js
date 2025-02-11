import Login from "./components/Login/Login.js";
import Register from "./components/Register/Register";
import ChatList from "./components/Chat/ChatList";
import { Routes, Route } from "react-router-dom";
import "./App.css";

function App() {
  return (
    <Routes>
      <Route path="/" element={<ChatList />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
    </Routes>
  );
}

export default App;
