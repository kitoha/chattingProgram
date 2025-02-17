import React, { useEffect, useState } from "react";
import BottomNav from "../common/BottomNav";
import { UserIcon } from "@heroicons/react/24/solid";

const ReceivedFriendList = () => {
  const [friendRequests, setFriendRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const BASE_URL = "http://localhost:8080";

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
          friendRequests.filter((req) => req.requestId !== requestId)
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
          friendRequests.filter((req) => req.requestId !== requestId)
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
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <header className="bg-white shadow-md p-4 flex items-center justify-between">
        <h2 className="text-lg font-bold text-gray-900">받은 친구 요청</h2>
      </header>

      {loading && (
        <p className="text-center text-gray-600 mt-4">
          친구 요청 목록을 불러오는 중...
        </p>
      )}
      {error && <p className="text-center text-red-500 mt-4">{error}</p>}

      <div className="flex-1 overflow-y-auto p-4">
        {friendRequests.length > 0 ? (
          friendRequests.map((request) => (
            <div
              key={request.requestId}
              className="bg-white shadow-md rounded-lg p-4 flex items-center justify-between mb-4"
            >
              <div className="flex items-center space-x-4">
                <div className="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center">
                  <UserIcon className="w-6 h-6 text-gray-700" />
                </div>
                <div>
                  <span className="text-lg font-semibold text-gray-900">
                    {request.fromUsername}
                  </span>
                  <p className="text-sm text-gray-500">친구 요청</p>
                </div>
              </div>
              <div className="flex space-x-2">
                <button
                  className="bg-green-500 text-white px-4 py-2 rounded-lg text-sm font-semibold"
                  onClick={() => acceptRequest(request.requestId)}
                >
                  ✔ 수락
                </button>
                <button
                  className="bg-red-500 text-white px-4 py-2 rounded-lg text-sm font-semibold"
                  onClick={() => rejectRequest(request.requestId)}
                >
                  ✖ 거절
                </button>
              </div>
            </div>
          ))
        ) : (
          <p className="text-center text-gray-500 mt-4">
            받은 친구 요청이 없습니다.
          </p>
        )}
      </div>

      <BottomNav activePage="requests" />
    </div>
  );
};

export default ReceivedFriendList;
