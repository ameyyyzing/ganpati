import axios from "axios";
export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "https://ganpati-backend-9q2m.onrender.com/api",
});
