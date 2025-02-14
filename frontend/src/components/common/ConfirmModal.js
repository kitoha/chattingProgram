import React from "react";
import "./ConfirmModal.css"; // 스타일 추가

const ConfirmModal = ({ isOpen, message, onConfirm, onCancel }) => {
  if (!isOpen) return null; // 모달이 열려 있지 않으면 렌더링하지 않음

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h3>확인</h3>
        <p>{message}</p>
        <div className="modal-buttons">
          <button className="modal-delete-button" onClick={onConfirm}>
            확인
          </button>
          <button className="modal-cancel-button" onClick={onCancel}>
            취소
          </button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmModal;
