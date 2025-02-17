import React from "react";
import { useNavigate } from "react-router-dom";
import {
  ChatBubbleLeftIcon,
  UsersIcon,
  InboxArrowDownIcon,
} from "@heroicons/react/24/solid";

const BottomNav = ({ activePage }) => {
  const navigate = useNavigate();

  return (
    <nav className="fixed bottom-0 w-full bg-white shadow-md border-t flex justify-around py-3">
      <button
        className={`flex flex-col items-center text-gray-500 ${
          activePage === "chat" ? "text-blue-500 font-semibold" : ""
        }`}
        onClick={() => navigate("/chatlist")}
      >
        <ChatBubbleLeftIcon className="w-6 h-6" />
        <span className="text-sm">채팅</span>
      </button>

      <button
        className={`flex flex-col items-center text-gray-500 ${
          activePage === "friends" ? "text-blue-500 font-semibold" : ""
        }`}
        onClick={() => navigate("/friends")}
      >
        <UsersIcon className="w-6 h-6" />
        <span className="text-sm">친구 목록</span>
      </button>

      <button
        className={`flex flex-col items-center text-gray-500 ${
          activePage === "requests" ? "text-blue-500 font-semibold" : ""
        }`}
        onClick={() => navigate("/received-friends")}
      >
        <InboxArrowDownIcon className="w-6 h-6" />
        <span className="text-sm">받은 요청</span>
      </button>
    </nav>
  );
};

export default BottomNav;
