import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import BottomNav from "../common/BottomNav";
import FriendModal from "../friend/FriendModal";
import GroupNameConfirmModal from "../friend/GroupNameConfirmModal";
import GroupNameInputModal from "../friend/GroupNameInputModal";

const BASE_URL = "http://localhost:8080";

const ChatList = () => {
  const navigate = useNavigate();
  const currentUser = useSelector((state) => state.user.username);

  // 채팅 목록 관련 상태
  const [activeTab, setActiveTab] = useState("oneToOne");
  const [oneToOneChats, setOneToOneChats] = useState([]);
  const [groupChats, setGroupChats] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 그룹 생성 및 친구 목록 관련 상태
  const [showFriendModal, setShowFriendModal] = useState(false);
  const [friends, setFriends] = useState([]);
  const [friendLoading, setFriendLoading] = useState(false);
  const [friendError, setFriendError] = useState(null);
  const [selectedFriends, setSelectedFriends] = useState([]);

  const [showGroupNameConfirmModal, setShowGroupNameConfirmModal] =
    useState(false);
  const [showGroupNameInputModal, setShowGroupNameInputModal] = useState(false);
  const [groupName, setGroupName] = useState("");

  const fetchChatRooms = async () => {
    if (!currentUser) return;
    setLoading(true);
    try {
      const response = await fetch(`${BASE_URL}/api/chatrooms`, {
        method: "GET",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
      });
      const result = await response.json();
      if (response.ok) {
        const oneToOne = result.data.filter(
          (room) => room.roomType === "ONE_TO_ONE"
        );
        const group = result.data.filter((room) => room.roomType === "GROUP");
        setOneToOneChats(oneToOne);
        setGroupChats(group);
      } else {
        throw new Error(result.message);
      }
    } catch (error) {
      setError("채팅 목록을 불러오는 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchChatRooms();
  }, [currentUser]);

  const fetchFriends = async () => {
    setFriendLoading(true);
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
        setFriendError(result.message || "친구 목록을 불러오지 못했습니다.");
      }
    } catch (error) {
      setFriendError("친구 목록을 불러오지 못했습니다.");
    } finally {
      setFriendLoading(false);
    }
  };

  useEffect(() => {
    if (showFriendModal) {
      fetchFriends();
    }
  }, [showFriendModal]);

  const toggleFriendSelection = (friendId) => {
    if (selectedFriends.includes(friendId)) {
      setSelectedFriends(selectedFriends.filter((id) => id !== friendId));
    } else {
      setSelectedFriends([...selectedFriends, friendId]);
    }
  };

  const createGroupChat = async (name) => {
    try {
      const response = await fetch(`${BASE_URL}/api/chatrooms/group`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify({
          groupName: name,
          participantIds: selectedFriends,
        }),
      });
      const result = await response.json();
      if (response.ok) {
        fetchChatRooms();
      } else {
        alert(result.message || "그룹 채팅 생성에 실패했습니다.");
      }
    } catch (error) {
      alert("그룹 채팅 생성에 실패했습니다.");
    }
    setSelectedFriends([]);
    setGroupName("");
  };

  // 나가기 버튼 클릭 시 호출할 함수 (백엔드 API: /api/chatrooms/leave)
  const leaveChatRoom = async (chatRoomId) => {
    try {
      const response = await fetch(`${BASE_URL}/api/chatrooms/leave`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        // 요청 바디는 chatRoomId (숫자)
        body: JSON.stringify(chatRoomId),
      });
      const result = await response.json();
      if (response.ok) {
        fetchChatRooms();
      } else {
        alert(result.message || "채팅방 나가기 실패");
      }
    } catch (error) {
      alert("채팅방 나가기 실패");
    }
  };

  const truncateMessage = (message) => {
    if (!message) return "메시지가 없습니다.";
    return message.length > 10 ? message.substring(0, 10) + "..." : message;
  };

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col relative">
      <header className="bg-white shadow-md p-4 flex items-center justify-between">
        <button
          className="text-gray-700 hover:text-gray-900"
          onClick={() => navigate("/")}
        >
          ←
        </button>
        <h2 className="text-lg font-bold text-gray-900">채팅 목록</h2>
        <div></div>
      </header>

      <div className="flex justify-around bg-white shadow-md p-2">
        <button
          className={`px-4 py-2 rounded-lg text-sm font-semibold ${
            activeTab === "oneToOne"
              ? "bg-black text-white"
              : "bg-gray-200 text-gray-700"
          }`}
          onClick={() => setActiveTab("oneToOne")}
        >
          1 대 1 채팅
        </button>
        <button
          className={`px-4 py-2 rounded-lg text-sm font-semibold ${
            activeTab === "group"
              ? "bg-black text-white"
              : "bg-gray-200 text-gray-700"
          }`}
          onClick={() => setActiveTab("group")}
        >
          그룹 채팅
        </button>
      </div>

      {loading && (
        <p className="text-center text-gray-600 mt-4">
          채팅 목록을 불러오는 중...
        </p>
      )}
      {error && <p className="text-center text-red-500 mt-4">{error}</p>}

      <div className="flex-1 overflow-y-auto p-4">
        {activeTab === "oneToOne" &&
          oneToOneChats.map((room) => (
            <div
              key={room.id}
              className="bg-white shadow-md rounded-lg p-4 flex items-center justify-between mb-4"
            >
              <div className="flex flex-col">
                <span className="text-lg font-semibold text-gray-900">
                  {room.name || "1:1 채팅"}
                </span>
                <span className="text-sm text-gray-500">
                  {truncateMessage(room.lastMessage?.content)}
                </span>
              </div>
              <div className="flex space-x-2">
                <button
                  className="bg-blue-500 text-white px-4 py-2 rounded-lg text-sm font-semibold"
                  onClick={() => navigate(`/chat/${room.id}`)}
                >
                  대화하기
                </button>
                <button
                  className="bg-gray-300 text-gray-900 px-4 py-2 rounded-lg text-sm font-semibold"
                  onClick={() => leaveChatRoom(room.id)}
                >
                  나가기
                </button>
              </div>
            </div>
          ))}

        {activeTab === "group" &&
          groupChats.map((room) => (
            <div
              key={room.id}
              className="bg-white shadow-md rounded-lg p-4 flex items-center justify-between mb-4"
            >
              <div className="flex flex-col">
                <span className="text-lg font-semibold text-gray-900">
                  {room.name}
                </span>
                <span className="text-sm text-gray-500">
                  {truncateMessage(room.lastMessage?.content)}
                </span>
              </div>
              <div className="flex items-center space-x-2">
                <button
                  className="bg-blue-500 text-white px-4 py-2 rounded-lg text-sm font-semibold"
                  onClick={() => navigate(`/chat/${room.id}`)}
                >
                  대화하기
                </button>
                <button
                  className="bg-gray-300 text-gray-900 px-4 py-2 rounded-lg text-sm font-semibold"
                  onClick={() => leaveChatRoom(room.id)}
                >
                  나가기
                </button>
                <span className="text-sm text-gray-500">
                  {room.participantCount}명
                </span>
              </div>
            </div>
          ))}
      </div>

      <BottomNav activePage="chat" />

      {activeTab === "group" && (
        <button
          className="fixed bottom-20 right-4 bg-green-500 text-white px-4 py-2 rounded-full shadow-lg"
          onClick={() => setShowFriendModal(true)}
        >
          그룹 채팅 생성
        </button>
      )}

      {showFriendModal && (
        <FriendModal
          friends={friends}
          friendLoading={friendLoading}
          friendError={friendError}
          selectedFriends={selectedFriends}
          toggleFriendSelection={toggleFriendSelection}
          onCancel={() => {
            setShowFriendModal(false);
            setSelectedFriends([]);
          }}
          onConfirm={() => {
            if (selectedFriends.length === 0) {
              alert("친구를 하나 이상 선택해주세요.");
              return;
            }
            setShowFriendModal(false);
            setShowGroupNameConfirmModal(true);
          }}
        />
      )}

      {showGroupNameConfirmModal && (
        <GroupNameConfirmModal
          onCancel={() => {
            setShowGroupNameConfirmModal(false);
            createGroupChat("새 그룹 채팅");
          }}
          onConfirm={() => {
            setShowGroupNameConfirmModal(false);
            setShowGroupNameInputModal(true);
          }}
        />
      )}

      {showGroupNameInputModal && (
        <GroupNameInputModal
          groupName={groupName}
          setGroupName={setGroupName}
          onCancel={() => {
            setShowGroupNameInputModal(false);
            setGroupName("");
          }}
          onConfirm={() => {
            if (!groupName.trim()) {
              alert("그룹 이름을 입력해주세요.");
              return;
            }
            setShowGroupNameInputModal(false);
            createGroupChat(groupName);
          }}
        />
      )}
    </div>
  );
};

export default ChatList;
