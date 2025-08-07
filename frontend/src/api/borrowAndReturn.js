import axios from 'axios';

const API_BASE = 'http://localhost:8080/api/borrowing'; // 根路徑

// 取得 JWT Token
const getAuthHeader = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
    }
});

// 借書
export const borrowBook = async (inventoryId) => {
    return axios.post(`${API_BASE}/borrow`, { inventoryId }, getAuthHeader());
};

// 還書
export const returnBook = async (inventoryId) => {
    return axios.post(`${API_BASE}/return`, { inventoryId }, getAuthHeader());
};

// 查詢尚未還的書(也就是用戶他借閱的書籍)
export const getUserBorrowedBooks = async () => {
    return axios.get(`${API_BASE}/current`, getAuthHeader());
};
