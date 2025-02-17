import React, { useEffect, useState, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import BottomNav from "../common/BottomNav";

const BASE_URL = "http://localhost:8080";

const ChatRoom = () => {
  const { chatRoomId } = useParams();
  const navigate = useNavigate();
  const currentUser = useSelector((state) => state.user.username);

  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [stompClient, setStompClient] = useState(null);
  const chatEndRef = useRef(null);

  useEffect(() => {
    if (!currentUser) return;

    const fetchMessages = async () => {
      try {
        const response = await fetch(
          `${BASE_URL}/api/chatrooms/${chatRoomId}/messages`,
          {
            method: "GET",
            credentials: "include",
            headers: {
              "Content-Type": "application/json",
              Accept: "application/json",
            },
          }
        );

        const result = await response.json();
        if (response.ok) {
          setMessages(result.data);
        } else {
          throw new Error(result.message);
        }
      } catch (error) {
        console.error("채팅 메시지를 불러오는 중 오류 발생:", error);
      }
    };

    fetchMessages();
    connectWebSocket();

    return () => {
      if (stompClient) {
        stompClient.deactivate();
      }
    };
  }, [chatRoomId, currentUser]);

  const connectWebSocket = () => {
    const socket = new SockJS(`${BASE_URL}/ws`);
    const client = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("웹소켓 연결 성공");
        client.subscribe(`/topic/messages.${chatRoomId}`, (message) => {
          const receivedMessage = JSON.parse(message.body);
          setMessages((prevMessages) => [...prevMessages, receivedMessage]);
        });
      },
      onDisconnect: () => {
        console.log("웹소켓 연결 종료");
      },
    });

    client.activate();
    setStompClient(client);
  };

  const sendMessage = () => {
    if (!newMessage.trim() || !stompClient || !stompClient.connected) {
      return;
    }

    const chatMessage = {
      chatRoomId: Number(chatRoomId),
      sender: currentUser,
      content: newMessage,
    };

    stompClient.publish({
      destination: `/app/chat.${chatRoomId}`,
      body: JSON.stringify(chatMessage),
    });

    setNewMessage("");
  };

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <div className="flex flex-col h-screen bg-gray-100">
      <header className="bg-white shadow-md p-4 flex items-center justify-between">
        <button
          className="text-gray-700 hover:text-gray-900"
          onClick={() => navigate("/chatlist")}
        >
          ←
        </button>
        <h2 className="text-lg font-bold text-gray-900">채팅방</h2>
        <div></div>
      </header>

      <div className="flex-1 overflow-y-auto p-4 max-h-[calc(100vh-120px)]">
        {messages.map((msg, index) => (
          <div
            key={index}
            className={`flex ${
              msg.sender === currentUser ? "justify-end" : "justify-start"
            } mb-2`}
          >
            <div
              className={`rounded-lg p-3 max-w-xs ${
                msg.sender === currentUser
                  ? "bg-blue-500 text-white"
                  : "bg-gray-300 text-gray-800"
              }`}
            >
              {msg.content}
            </div>
          </div>
        ))}
        <div ref={chatEndRef} />
      </div>

      <div className="bg-white p-3 flex items-center border-t sticky bottom-16 mb-4">
        <input
          type="text"
          placeholder="메시지를 입력하세요..."
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          className="flex-1 border border-gray-300 rounded-full px-4 py-2 text-gray-700 outline-none"
        />
        <button
          className="ml-3 bg-blue-500 text-white px-4 py-2 rounded-full"
          onClick={sendMessage}
        >
          📩
        </button>
      </div>

      <BottomNav activePage="chat" />
    </div>
  );
};

export default ChatRoom;
