import axios from 'axios';

const API_BASE = 'http://localhost:8080/api/borrowing';

// 取得 JWT Token
const getAuthHeader = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
    }
});

// ✅ 借書（不需要傳 userId）
export const borrowBook = async (inventoryId) => {
    return axios.post(`${API_BASE}/borrow`, { inventoryId }, getAuthHeader());
};

// ✅ 還書（不需要傳 userId）
export const returnBook = async (inventoryId) => {
    return axios.post(`${API_BASE}/return`, { inventoryId }, getAuthHeader());
};

// ✅ 查詢尚未還書清單（GET，不帶 userId）
export const getUserBorrowedBooks = async () => {
    return axios.get(`${API_BASE}/current`, getAuthHeader());
};
