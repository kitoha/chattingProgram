import React, { useEffect, useState, useRef } from "react";
import { useParams } from "react-router-dom";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import "./ChatRoom.css";

const BASE_URL = "http://localhost:8080";

const ChatRoom = () => {
  const { chatRoomId } = useParams();
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [stompClient, setStompClient] = useState(null);
  const chatEndRef = useRef(null);

  const sender = "사용자";
  const receiver = "상대방";

  useEffect(() => {
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
  }, [chatRoomId]);

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
      sender: sender,
      content: newMessage,
      receiver: receiver,
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
    <div className="chat-container">
      {/* 채팅방 헤더 */}
      <header className="chat-header">
        <h2>채팅방</h2>
      </header>

      {/* 채팅 메시지 리스트 */}
      <div className="chat-messages">
        {messages.map((msg, index) => (
          <div
            key={index}
            className={`chat-message ${
              msg.sender === sender ? "my-message" : "other-message"
            }`}
          >
            <div className="chat-bubble">
              <p className="chat-text">{msg.content}</p>
            </div>
          </div>
        ))}
        <div ref={chatEndRef} />
      </div>

      {/* 채팅 입력창 */}
      <div className="chat-input-container">
        <input
          type="text"
          placeholder="메시지를 입력하세요."
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          className="chat-input"
        />
        <button className="send-button" onClick={sendMessage}>
          📩
        </button>
      </div>
    </div>
  );
};

export default ChatRoom;
