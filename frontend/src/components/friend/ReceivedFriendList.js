import React, { useEffect, useState } from "react";
import BottomNav from "../common/BottomNav";
import "./ReceivedFriendList.css";

const ReceivedFriendList = () => {
  // 받은 친구 요청 목록 상태
  const [friendRequests, setFriendRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // API 호출 시 사용할 BASE_URL 설정
  const BASE_URL = "http://localhost:8080";

  // 친구 요청 목록 API 호출
  useEffect(() => {
    const fetchFriendRequests = async () => {
      try {
        const response = await fetch(`${BASE_URL}/api/friends/requests`, {
          method: "GET",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
        });

        const result = await response.json();

        if (response.ok) {
          setFriendRequests(result.data);
        } else {
          throw new Error(result.message);
        }
      } catch (error) {
        setError("친구 요청 목록을 불러오지 못했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchFriendRequests();
  }, []);

  // 친구 요청 수락 (ProcessFriendRequestDto 사용)
  const acceptRequest = async (requestId) => {
    try {
      const response = await fetch(`${BASE_URL}/api/friends/request/accept`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify({ requestId }),
      });

      if (response.ok) {
        setFriendRequests(
          friendRequests.filter((request) => request.requestId !== requestId)
        );
        alert("친구 요청을 수락했습니다!");
      } else {
        throw new Error("친구 요청 수락에 실패했습니다.");
      }
    } catch (error) {
      alert("서버 오류가 발생했습니다.");
    }
  };

  const rejectRequest = async (requestId) => {
    try {
      const response = await fetch(`${BASE_URL}/api/friends/request/reject`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: JSON.stringify({ requestId }),
      });

      if (response.ok) {
        setFriendRequests(
          friendRequests.filter((request) => request.requestId !== requestId)
        );
        alert("친구 요청을 거절했습니다.");
      } else {
        throw new Error("친구 요청 거절에 실패했습니다.");
      }
    } catch (error) {
      alert("서버 오류가 발생했습니다.");
    }
  };

  return (
    <div className="received-container">
      {/* 헤더 */}
      <header className="received-header">
        <h2>받은 친구 요청</h2>
      </header>

      {/* 로딩 상태 */}
      {loading && (
        <p className="loading-text">친구 요청 목록을 불러오는 중...</p>
      )}
      {error && <p className="error-text">{error}</p>}

      {/* 받은 친구 요청 목록 */}
      <div className="received-list">
        {friendRequests.length > 0 ? (
          friendRequests.map((request) => (
            <div key={request.requestId} className="received-item">
              {/* ✅ 아이콘 사용 */}
              <div className="received-icon">👤</div>
              <div className="received-info">
                <div className="received-name">{request.fromUsername}</div>
              </div>
              <div className="button-container">
                <button
                  className="accept-button"
                  onClick={() => acceptRequest(request.requestId)}
                >
                  ✔
                </button>
                <button
                  className="reject-button"
                  onClick={() => rejectRequest(request.requestId)}
                >
                  ✖
                </button>
              </div>
            </div>
          ))
        ) : (
          <p className="no-requests">받은 친구 요청이 없습니다.</p>
        )}
      </div>

      {/* 하단 네비게이션 */}
      <BottomNav activePage="requests" />
    </div>
  );
};

export default ReceivedFriendList;
