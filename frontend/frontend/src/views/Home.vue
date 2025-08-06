<template>
    <div class="container mt-5">
        <h1>歡迎來到圖書系統</h1>
        <router-link to="/" class="btn btn-primary me-2">登入</router-link>
        <router-link to="/register" class="btn btn-secondary">註冊</router-link>
    </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

onMounted(() => {
    const token = localStorage.getItem('token')
    if (token) {
        router.push('/borrow') // 已登入就導向主頁（借書頁）
    }
})
</script>