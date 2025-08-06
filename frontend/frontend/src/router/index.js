import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'// 將Login.vue引入進來
import Register from '../views/Register.vue' // 將Register.vue引入進來
import Borrow from '../views/BorrowPage.vue';
import Return from '../views/ReturnPage.vue';

// 配置路徑
const routes = [
  { path: '/', name: 'Login', component: Login }, // 登入頁面
  { path: '/register', name: 'Register', component: Register }, // 註冊頁面
  { path: '/borrow', name: 'Borrow', component: Borrow },
  { path: '/return', name: 'Return', component: Return }
]


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
})

export default router
