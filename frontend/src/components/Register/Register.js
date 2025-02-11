import React, { useState } from "react";
import { register } from "../../services/authService";
import { useNavigate } from "react-router-dom";
import "./Register.css";

const Register = () => {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
  });

  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const response = await register(formData);

    if (response.statusCode === 201) {
      alert("회원가입 성공! 로그인 페이지로 이동합니다.");
      navigate("/"); // 회원가입 성공 시 로그인 페이지로 이동
    } else {
      setError(response.message || "회원가입 실패");
    }
  };

  return (
    <div className="container">
      <div className="register-box">
        <h2 className="title">회원가입</h2>
        {error && <p className="error-message">{error}</p>}
        <input
          type="text"
          name="username"
          placeholder="사용자 이름"
          className="input-field"
          onChange={handleChange}
        />
        <input
          type="email"
          name="email"
          placeholder="이메일"
          className="input-field"
          onChange={handleChange}
        />
        <input
          type="password"
          name="password"
          placeholder="비밀번호"
          className="input-field"
          onChange={handleChange}
        />
        <button className="register-button" onClick={handleSubmit}>
          회원가입
        </button>

        <p className="login-text">이미 계정이 있으신가요?</p>
        <button className="login-button" onClick={() => navigate("/")}>
          로그인 페이지로 이동
        </button>
      </div>
    </div>
  );
};

export default Register;
