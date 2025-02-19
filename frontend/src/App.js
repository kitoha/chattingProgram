import Login from "./components/Login/Login";
import Register from "./components/Register/Register";
import ChatList from "./components/Chat/ChatList";
import FriendList from "./components/friend/FriendList";
import ReceivedFriendList from "./components/friend/ReceivedFriendList";
import ChatRoom from "./components/Chat/ChatRoom";
import { Routes, Route } from "react-router-dom";
import "./App.css";

function App() {
  return (
    <Routes>
      <Route path="/chatlist" element={<ChatList />} />
      <Route path="/" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/friends" element={<FriendList />} />
      <Route path="/received-friends" element={<ReceivedFriendList />} />
      <Route path="/chat/:chatRoomId" element={<ChatRoom />} />
    </Routes>
  );
}

export default App;
