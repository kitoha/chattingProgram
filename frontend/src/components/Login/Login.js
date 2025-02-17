import React, { useState } from "react";
import { login } from "../../services/authService";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { setUser } from "../common/userSlice";

const Login = () => {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
  });

  const [error, setError] = useState("");
  const navigate = useNavigate();
  const dispatch = useDispatch();

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
      dispatch(setUser(response.data));
      navigate("/chatlist");
    } else {
      setError(response.message || "로그인 실패");
    }
  };

  return (
    <div className="flex min-h-screen bg-gray-100 items-center justify-center">
      <div className="w-full max-w-md bg-white shadow-md rounded-lg p-6">
        <h2 className="text-2xl font-bold text-center text-gray-900">로그인</h2>

        {error && <p className="text-red-500 text-center mt-2">{error}</p>}

        <div className="mt-4">
          <input
            type="text"
            name="username"
            placeholder="사용자 이름"
            className="w-full p-3 border rounded-lg text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
            onChange={handleChange}
          />
        </div>
        <div className="mt-4">
          <input
            type="email"
            name="email"
            placeholder="이메일"
            className="w-full p-3 border rounded-lg text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
            onChange={handleChange}
          />
        </div>
        <div className="mt-4">
          <input
            type="password"
            name="password"
            placeholder="비밀번호"
            className="w-full p-3 border rounded-lg text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
            onChange={handleChange}
          />
        </div>

        <button
          className="w-full bg-blue-500 hover:bg-blue-600 text-white font-semibold py-3 rounded-lg mt-6"
          onClick={handleSubmit}
        >
          로그인
        </button>

        <p className="text-center text-gray-600 mt-4">계정이 없으신가요?</p>

        <button
          className="w-full bg-gray-300 hover:bg-gray-400 text-gray-900 font-semibold py-3 rounded-lg mt-2"
          onClick={() => navigate("/register")}
        >
          회원가입
        </button>
      </div>
    </div>
  );
};

export default Login;
