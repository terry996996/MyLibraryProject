import axios from 'axios'

export const fetchBooksWithInventory = () => {
    const token = localStorage.getItem('token');

    return axios.get('http://localhost:8080/api/books/with-inventory', {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });
};