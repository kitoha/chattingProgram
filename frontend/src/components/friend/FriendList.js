import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import BottomNav from "../common/BottomNav";
import ConfirmModal from "../common/ConfirmModal";
import "./FriendList.css";

const FriendList = () => {
  const navigate = useNavigate();
  const [friends, setFriends] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const BASE_URL = "http://localhost:8080";

  useEffect(() => {
    const fetchFriends = async () => {
      try {
        const response = await fetch(`${BASE_URL}/api/friends`, {
          method: "GET",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
        });

        const result = await response.json();

        if (response.ok) {
          setFriends(result.data);
        } else {
          throw new Error(result.message);
        }
      } catch (error) {
        setError("친구 목록을 불러오지 못했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchFriends();
  }, []);

  const startChat = async (friendId) => {
    try {
      const response = await fetch(`${BASE_URL}/api/chatrooms/one-to-one`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify(friendId),
      });

      if (response.ok) {
        const result = await response.json();
        const chatRoomId = result.data.id;
        navigate(`/chat/${chatRoomId}`);
      } else {
        throw new Error("채팅방 개설에 실패했습니다.");
      }
    } catch (error) {
      alert("서버 오류가 발생했습니다.");
    }
  };

  return (
    <div className="friend-container">
      <header className="friend-header">
        <h2>친구 목록</h2>
      </header>

      <div className="friend-list">
        {loading ? (
          <p className="loading-text">친구 목록을 불러오는 중...</p>
        ) : error ? (
          <p className="error-text">{error}</p>
        ) : friends.length > 0 ? (
          friends.map((friend) => (
            <div key={friend.friendId} className="friend-item">
              <img
                src="/images/default-avatar.png"
                alt={friend.friendUsername}
                className="friend-avatar"
              />
              <div className="friend-info">
                <div className="friend-name">{friend.friendUsername}</div>
              </div>
              <div className="button-container">
                <button
                  className="chat-button"
                  onClick={() => startChat(friend.friendId)}
                >
                  대화하기
                </button>
              </div>
            </div>
          ))
        ) : (
          <p className="no-friends">친구가 없습니다.</p>
        )}
      </div>

      <BottomNav activePage="friends" />
    </div>
  );
};

export default FriendList;
