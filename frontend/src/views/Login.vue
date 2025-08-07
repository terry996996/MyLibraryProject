<template>
    <!-- 引入登入頁組件 -->
    <LoginForm />
</template>

<script setup>
import LoginForm from '../components/LoginForm.vue'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

onMounted(() => {
    const token = localStorage.getItem('token')
    // 身分驗證，如果在localSorage找到有存token，代表是有登入
    if (token) {
        router.push('/borrow') // 用戶如果是已登入的狀態就直接導往借書頁
    }
})

</script>