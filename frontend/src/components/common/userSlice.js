import { createSlice } from "@reduxjs/toolkit";

const storedUser = sessionStorage.getItem("currentUser");

const initialState = {
  username: storedUser ? storedUser : null,
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUser: (state, action) => {
      state.username = action.payload;
      sessionStorage.setItem("currentUser", action.payload);
    },
    logoutUser: (state) => {
      state.username = null;
      sessionStorage.removeItem("currentUser");
    },
  },
});

export const { setUser, logoutUser } = userSlice.actions;
export default userSlice.reducer;
