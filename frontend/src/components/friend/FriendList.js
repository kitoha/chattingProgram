import React, { useState } from "react";
import BottomNav from "../common/BottomNav";
import "./FriendList.css"; // 스타일 파일

const FriendList = () => {
  // 친구 목록
  const [friends, setFriends] = useState([
    { id: 1, name: "김철수", img: "/images/friend1.jpg" },
    { id: 2, name: "이영희", img: "/images/friend2.jpg" },
  ]);

  // 친구 추가 팝업 상태
  const [showPopup, setShowPopup] = useState(false);
  const [newFriendName, setNewFriendName] = useState("");

  // 팝업 열기
  const openPopup = () => {
    setShowPopup(true);
    setNewFriendName("");
  };

  // 팝업 닫기
  const closePopup = () => {
    setShowPopup(false);
  };

  // 친구 추가
  const addFriend = () => {
    if (newFriendName.trim() === "") {
      alert("친구 이름을 입력해주세요!");
      return;
    }

    const newFriend = {
      id: friends.length + 1,
      name: newFriendName,
      img: "/images/default-avatar.png",
    };

    setFriends([...friends, newFriend]);
    closePopup();
  };

  return (
    <div className="friend-container">
      {/* 헤더 */}
      <header className="friend-header">
        <h2>친구 목록</h2>
        <button className="add-friend-button" onClick={openPopup}>
          + 친구 추가
        </button>
      </header>

      {/* 팝업 창 */}
      {showPopup && (
        <div className="popup-overlay">
          <div className="popup-content">
            {/* ✅ 팝업 헤더 */}
            <h3 className="popup-title">친구 추가</h3>

            {/* ✅ 입력란 */}
            <input
              type="text"
              placeholder="친구 이름 입력"
              value={newFriendName}
              onChange={(e) => setNewFriendName(e.target.value)}
              className="popup-input"
            />

            {/* ✅ 버튼 영역 */}
            <div className="popup-buttons">
              <button className="popup-add-button" onClick={addFriend}>
                추가
              </button>
              <button className="popup-cancel-button" onClick={closePopup}>
                취소
              </button>
            </div>
          </div>
        </div>
      )}

      {/* 친구 목록 */}
      <div className="friend-list">
        {friends.map((friend) => (
          <div key={friend.id} className="friend-item">
            <img src={friend.img} alt={friend.name} className="friend-avatar" />
            <div className="friend-info">
              <div className="friend-name">{friend.name}</div>
            </div>
          </div>
        ))}
      </div>

      {/* 하단 네비게이션 */}
      <BottomNav activePage="friends" />
    </div>
  );
};

export default FriendList;
