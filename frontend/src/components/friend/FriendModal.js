import React from "react";
import Modal from "../common/Modal";

const FriendModal = ({
  friends,
  friendLoading,
  friendError,
  selectedFriends,
  toggleFriendSelection,
  onCancel,
  onConfirm,
}) => (
  <Modal onClose={onCancel}>
    <h3 className="text-lg font-bold mb-4">친구 목록</h3>
    {friendLoading ? (
      <p>로딩중...</p>
    ) : friendError ? (
      <p className="text-red-500">{friendError}</p>
    ) : (
      <div className="max-h-60 overflow-y-auto">
        {friends.map((friend) => (
          <div key={friend.id} className="flex items-center mb-2">
            <input
              type="checkbox"
              checked={selectedFriends.includes(friend.id)}
              onChange={() => toggleFriendSelection(friend.id)}
              className="form-checkbox h-5 w-5 text-blue-600"
            />
            <span className="ml-2">{friend.name}</span>
          </div>
        ))}
      </div>
    )}
    <div className="flex justify-end mt-4 space-x-2">
      <button
        className="px-4 py-2 bg-gray-300 text-gray-700 rounded"
        onClick={onCancel}
      >
        취소
      </button>
      <button
        className="px-4 py-2 bg-blue-500 text-white rounded"
        onClick={onConfirm}
      >
        생성하기
      </button>
    </div>
  </Modal>
);

export default FriendModal;
