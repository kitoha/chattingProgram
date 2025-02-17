import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import BottomNav from "../common/BottomNav";

const BASE_URL = "http://localhost:8080";

const ChatList = () => {
  const navigate = useNavigate();
  const currentUser = useSelector((state) => state.user.username);
  const [activeTab, setActiveTab] = useState("oneToOne");
  const [oneToOneChats, setOneToOneChats] = useState([]);
  const [groupChats, setGroupChats] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!currentUser) return;

    const fetchChatRooms = async () => {
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
        setError("채팅 목록을 불러오지 못했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchChatRooms();
  }, [currentUser]);

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      {/* 헤더 */}
      <header className="bg-white shadow-md p-4 flex items-center justify-between">
        <button
          className="text-gray-700 hover:text-gray-900"
          onClick={() => navigate("/")}
        >
          ←
        </button>
        <h2 className="text-lg font-bold text-gray-900">채팅 목록</h2>
        <div></div> {/* 빈 공간 */}
      </header>

      {/* 탭 네비게이션 */}
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

      {/* 로딩 및 오류 메시지 */}
      {loading && (
        <p className="text-center text-gray-600 mt-4">
          채팅 목록을 불러오는 중...
        </p>
      )}
      {error && <p className="text-center text-red-500 mt-4">{error}</p>}

      {/* 채팅 리스트 */}
      <div className="flex-1 overflow-y-auto p-4">
        {activeTab === "oneToOne" &&
          oneToOneChats.map((room) => (
            <div
              key={room.id}
              className="bg-white shadow-md rounded-lg p-4 flex items-center justify-between mb-4"
            >
              <div className="flex flex-col">
                <span className="text-lg font-semibold text-gray-900">
                  {room.name}
                </span>
                <span className="text-sm text-gray-500">안녕하세요</span>
              </div>
              <div className="flex space-x-2">
                <button
                  className="bg-blue-500 text-white px-4 py-2 rounded-lg text-sm font-semibold"
                  onClick={() => navigate(`/chat/${room.id}`)}
                >
                  대화하기
                </button>
                <button className="bg-gray-300 text-gray-900 px-4 py-2 rounded-lg text-sm font-semibold">
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
                <span className="text-sm text-gray-500">안녕하세요</span>
              </div>
              <div className="flex space-x-2">
                <button
                  className="bg-blue-500 text-white px-4 py-2 rounded-lg text-sm font-semibold"
                  onClick={() => navigate(`/chat/${room.id}`)}
                >
                  대화하기
                </button>
                <button className="bg-gray-300 text-gray-900 px-4 py-2 rounded-lg text-sm font-semibold">
                  나가기
                </button>
                <span className="text-sm text-gray-500">{room.members}명</span>
              </div>
            </div>
          ))}
      </div>

      {/* 하단 네비게이션 */}
      <BottomNav activePage="chat" />
    </div>
  );
};

export default ChatList;
