import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import BottomNav from "../common/BottomNav";

const BASE_URL = "http://localhost:8080";

const FriendList = () => {
  const navigate = useNavigate();
  const currentUser = useSelector((state) => state.user.username);
  const [friends, setFriends] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showAddFriendPopup, setShowAddFriendPopup] = useState(false);
  const [newFriendId, setNewFriendId] = useState("");

  useEffect(() => {
    if (!currentUser) return;

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
  }, [currentUser]);

  const openAddFriendPopup = () => {
    setShowAddFriendPopup(true);
    setNewFriendId("");
  };

  const closeAddFriendPopup = () => {
    setShowAddFriendPopup(false);
  };

  const sendFriendRequest = async () => {
    if (!newFriendId.trim()) {
      alert("친구 ID를 입력해주세요!");
      return;
    }

    try {
      const response = await fetch(
        `${BASE_URL}/api/friends/request/${newFriendId}`,
        {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
        }
      );

      if (response.ok) {
        alert("친구 요청이 전송되었습니다!");
      } else {
        throw new Error("친구 요청에 실패했습니다.");
      }
    } catch (error) {
      alert("서버 오류가 발생했습니다.");
    } finally {
      closeAddFriendPopup();
    }
  };

  const deleteFriend = async (friendId) => {
    try {
      const response = await fetch(
        `${BASE_URL}/api/friends/delete/${friendId}`,
        {
          method: "DELETE",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
        }
      );

      if (response.ok) {
        setFriends(friends.filter((friend) => friend.friendId !== friendId));
        alert("친구가 삭제되었습니다.");
      } else {
        throw new Error("친구 삭제에 실패했습니다.");
      }
    } catch (error) {
      alert("서버 오류가 발생했습니다.");
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <header className="bg-white shadow-md p-4 flex items-center justify-between">
        <button
          className="text-gray-700 hover:text-gray-900"
          onClick={() => navigate("/")}
        >
          ←
        </button>
        <h2 className="text-lg font-bold text-gray-900">친구 목록</h2>
        <button
          className="text-blue-500 font-semibold"
          onClick={openAddFriendPopup}
        >
          + 추가
        </button>
      </header>

      {showAddFriendPopup && (
        <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex justify-center items-center">
          <div className="bg-white p-6 rounded-lg shadow-lg w-80">
            <h3 className="text-lg font-bold mb-3">친구 추가</h3>
            <input
              type="text"
              placeholder="친구 ID 입력"
              value={newFriendId}
              onChange={(e) => setNewFriendId(e.target.value)}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 mb-4 outline-none"
            />
            <div className="flex justify-between">
              <button
                className="bg-blue-500 text-white px-4 py-2 rounded-lg w-full mr-2"
                onClick={sendFriendRequest}
              >
                추가
              </button>
              <button
                className="bg-gray-300 text-gray-900 px-4 py-2 rounded-lg w-full"
                onClick={closeAddFriendPopup}
              >
                취소
              </button>
            </div>
          </div>
        </div>
      )}

      <div className="flex-1 overflow-y-auto p-4">
        {loading ? (
          <p className="text-center text-gray-600">
            친구 목록을 불러오는 중...
          </p>
        ) : error ? (
          <p className="text-center text-red-500">{error}</p>
        ) : friends.length > 0 ? (
          friends.map((friend) => (
            <div
              key={friend.id}
              className="bg-white shadow-md rounded-lg p-4 flex items-center justify-between mb-4"
            >
              <div className="flex items-center space-x-4">
                <span className="text-lg font-semibold text-gray-900">
                  {friend.name}
                </span>
              </div>
              <div className="flex space-x-2">
                <button
                  className="bg-blue-500 text-white px-4 py-2 rounded-lg text-sm font-semibold"
                  onClick={() => navigate(`/chat/${friend.id}`)}
                >
                  대화하기
                </button>
                <button
                  className="bg-red-500 text-white px-4 py-2 rounded-lg text-sm font-semibold"
                  onClick={() => deleteFriend(friend.id)}
                >
                  삭제
                </button>
              </div>
            </div>
          ))
        ) : (
          <p className="text-center text-gray-600">친구가 없습니다.</p>
        )}
      </div>

      <BottomNav activePage="friends" />
    </div>
  );
};

export default FriendList;
