import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/auth";

// 회원가입 요청
export const register = async (userData) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/register`, userData);
    return response.data;
  } catch (error) {
    console.error("회원가입 실패:", error);
    return error.response?.data || { message: "회원가입 중 오류 발생" };
  }
};

// 로그인 요청
export const login = async (userData) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/login`, userData, {
      withCredentials: true,
    });
    return response.data;
  } catch (error) {
    console.error("로그인 실패:", error);
    return error.response?.data || { message: "로그인 중 오류 발생" };
  }
};
