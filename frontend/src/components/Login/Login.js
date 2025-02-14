import React, { useState } from "react";
import { login } from "../../services/authService";
import { useNavigate } from "react-router-dom";
import "./Login.css";

const Login = () => {
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
    const response = await login(formData);

    if (response.statusCode === 200) {
      alert("로그인 성공!");
      navigate("/chatlist");
    } else {
      setError(response.message || "로그인 실패");
    }
  };

  return (
    <div className="container">
      <div className="login-box">
        <h2 className="title">CHAT</h2>
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
        <button className="login-button" onClick={handleSubmit}>
          로그인
        </button>

        <p className="register-text">계정이 없으신가요?</p>
        <button
          className="register-button"
          onClick={() => navigate("/register")}
        >
          회원가입
        </button>
      </div>
    </div>
  );
};

export default Login;
