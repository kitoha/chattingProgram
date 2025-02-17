import React, { useState } from "react";
import { register } from "../../services/authService";
import { useNavigate } from "react-router-dom";

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
      navigate("/");
    } else {
      setError(response.message || "회원가입 실패");
    }
  };

  return (
    <div className="flex min-h-screen bg-gray-100 items-center justify-center">
      <div className="w-full max-w-md bg-white shadow-md rounded-lg p-6">
        <h2 className="text-2xl font-bold text-center text-gray-900">
          회원가입
        </h2>

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
          회원가입
        </button>

        <p className="text-center text-gray-600 mt-4">
          이미 계정이 있으신가요?
        </p>

        <button
          className="w-full bg-gray-300 hover:bg-gray-400 text-gray-900 font-semibold py-3 rounded-lg mt-2"
          onClick={() => navigate("/")}
        >
          로그인 페이지로 이동
        </button>
      </div>
    </div>
  );
};

export default Register;
