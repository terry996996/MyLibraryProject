import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'// 將Login.vue引入進來
import Register from '../views/Register.vue' // 將Register.vue引入進來
import Borrow from '../views/BorrowPage.vue'; // 將BorrowPage.vue引入進來
import Return from '../views/ReturnPage.vue'; // 將ReturnPage.vue引入進來

// 配置路徑
const routes = [
  { path: '/', name: 'Login', component: Login }, // 登入頁面 (我設定為網頁的根路徑)
  { path: '/register', name: 'Register', component: Register }, // 註冊頁面
  { path: '/borrow', name: 'Borrow', component: Borrow }, // 借書頁面
  { path: '/return', name: 'Return', component: Return } // 還書頁面
]


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
})

export default router
