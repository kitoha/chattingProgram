import React from "react";
import { useNavigate } from "react-router-dom";
import "./BottomNav.css"; // 스타일 파일

const BottomNav = ({ activePage }) => {
  const navigate = useNavigate();

  return (
    <nav className="bottom-nav">
      <button
        className={activePage === "chat" ? "active" : ""}
        onClick={() => navigate("/chatlist")}
      >
        💬 채팅
      </button>
      <button
        className={activePage === "friends" ? "active" : ""}
        onClick={() => navigate("/friends")}
      >
        👥 친구 목록
      </button>
      <button
        className={activePage === "requests" ? "active" : ""}
        onClick={() => navigate("/received-friends")}
      >
        📩 받은 친구 목록
      </button>
    </nav>
  );
};

export default BottomNav;
