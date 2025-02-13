import Login from "./components/login/Login.js";
import Register from "./components/register/Register";
import ChatList from "./components/chat/ChatList";
import FriendList from "./components/friend/FriendList";
import ReceivedFriendList from "./components/friend/ReceivedFriendList";
import { Routes, Route } from "react-router-dom";
import "./App.css";

function App() {
  return (
    <Routes>
      <Route path="/chatlist" element={<ChatList />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/friends" element={<FriendList />} />
      <Route path="/received-friends" element={<ReceivedFriendList />} />
    </Routes>
  );
}

export default App;
