import React from "react";
import Modal from "../common/Modal";

const GroupNameInputModal = ({
  groupName,
  setGroupName,
  onCancel,
  onConfirm,
}) => (
  <Modal onClose={onCancel}>
    <h3 className="text-lg font-bold mb-4">그룹 이름 입력</h3>
    <input
      type="text"
      className="w-full border rounded px-3 py-2 mb-4"
      value={groupName}
      onChange={(e) => setGroupName(e.target.value)}
      placeholder="그룹 이름"
    />
    <div className="flex justify-end space-x-2">
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
        확인
      </button>
    </div>
  </Modal>
);

export default GroupNameInputModal;
