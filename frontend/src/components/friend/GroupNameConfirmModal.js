import React from "react";
import Modal from "../common/Modal";

const GroupNameConfirmModal = ({ onCancel, onConfirm }) => (
  <Modal onClose={onCancel}>
    <p className="mb-4 text-center">그룹 이름을 설정할까요?</p>
    <div className="flex justify-end space-x-2">
      <button
        className="px-4 py-2 bg-gray-300 text-gray-700 rounded"
        onClick={onCancel}
      >
        아니요
      </button>
      <button
        className="px-4 py-2 bg-blue-500 text-white rounded"
        onClick={onConfirm}
      >
        예
      </button>
    </div>
  </Modal>
);

export default GroupNameConfirmModal;
