import React from "react";
import "./Login.css";

const Login = () => {
  return (
    <div className="container">
      <div className="login-box">
        <h2 className="title">CHAT</h2>
        <input
          type="text"
          placeholder="이메일 또는 아이디"
          className="input-field"
        />
        <input type="password" placeholder="비밀번호" className="input-field" />
        <button className="login-button">로그인</button>
      </div>
    </div>
  );
};

export default Login;
