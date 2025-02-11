import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./ChatList.css";

const ChatList = () => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState("oneToOne");

  // 1대1 채팅 리스트
  const oneToOneChats = [
    {
      id: 1,
      name: "너의목소리가들려",
      lastMessage: "><",
      time: "0분전",
      img: "/images/chat1.jpg",
    },
    {
      id: 2,
      name: "동심동실",
      lastMessage: "채팅방이 삭제되었습니다.",
      time: "1분전",
      img: "/images/chat2.jpg",
    },
    {
      id: 3,
      name: "지금몇시",
      lastMessage: "안녕하세요",
      time: "53분전",
      img: "/images/chat3.jpg",
    },
  ];

  // 그룹 채팅 리스트
  const groupChats = [
    {
      id: 101,
      name: "개발자 모임",
      lastMessage: "코드 리뷰 언제 하죠?",
      time: "30분전",
      img: "/images/chat4.jpg",
      members: 5,
    },
    {
      id: 102,
      name: "여행 동호회",
      lastMessage: "다음 여행지는 어디로 갈까요?",
      time: "1시간전",
      img: "/images/chat5.jpg",
      members: 8,
    },
  ];

  const handleDelete = (id) => {
    alert(`채팅방 ${id} 삭제`);
  };

  return (
    <div className="chat-container">
      {/* 헤더 */}
      <header className="chat-header">
        <h2>Chat List</h2>
      </header>

      {/* 탭 네비게이션 */}
      <div className="chat-tabs">
        <button
          className={activeTab === "oneToOne" ? "active" : ""}
          onClick={() => setActiveTab("oneToOne")}
        >
          1대1 채팅
        </button>
        <button
          className={activeTab === "group" ? "active" : ""}
          onClick={() => setActiveTab("group")}
        >
          그룹 채팅
        </button>
      </div>

      {/* 채팅 리스트 */}
      <div className="chat-list">
        {activeTab === "oneToOne" &&
          oneToOneChats.map((room) => (
            <div
              key={room.id}
              className="chat-item"
              onClick={() => navigate(`/chat/${room.id}`)}
            >
              <img src={room.img} alt={room.name} className="chat-avatar" />
              <div className="chat-info">
                <div className="chat-title">
                  {room.name} <span className="chat-time">{room.time}</span>
                </div>
                <div className="chat-message">{room.lastMessage}</div>
              </div>
              <button
                className="delete-button"
                onClick={(e) => {
                  e.stopPropagation();
                  handleDelete(room.id);
                }}
              >
                ×
              </button>
            </div>
          ))}

        {activeTab === "group" &&
          groupChats.map((room) => (
            <div
              key={room.id}
              className="chat-item"
              onClick={() => navigate(`/chat/${room.id}`)}
            >
              <img src={room.img} alt={room.name} className="chat-avatar" />
              <div className="chat-info">
                <div className="chat-title">
                  {room.name} <span className="chat-time">{room.time}</span>
                </div>
                <div className="chat-message">{room.lastMessage}</div>
                <div className="chat-members">👥 {room.members}명</div>
              </div>
              <button
                className="delete-button"
                onClick={(e) => {
                  e.stopPropagation();
                  handleDelete(room.id);
                }}
              >
                ×
              </button>
            </div>
          ))}
      </div>

      {/* 하단 네비게이션 */}
      <nav className="bottom-nav">
        <button onClick={() => navigate("/home")}>🏠 홈</button>
        <button className="active">💬 채팅</button>
        <button onClick={() => navigate("/profile")}>👤 내정보</button>
      </nav>
    </div>
  );
};

export default ChatList;
