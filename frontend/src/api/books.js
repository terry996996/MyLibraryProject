import axios from 'axios'

// 查找所有書籍資料 (調用後端API)，BorrowPage.vue會調這裡的方法，然後有加身分驗證(JWT)
// 代表在登入的狀態之下，訪問借書頁面，才會查出館藏書籍資料(為了一些安全性)
export const fetchBooksWithInventory = () => {
    const token = localStorage.getItem('token');

    return axios.get('http://localhost:8080/api/books/with-inventory', { // 接後端傳的資料(dto)進來(接收spring轉成的json格式)
        headers: {
            Authorization: `Bearer ${token}`
        }
    });
};