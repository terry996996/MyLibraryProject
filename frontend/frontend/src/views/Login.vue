<template>
    <LoginForm />
</template>

<script setup>
import LoginForm from '../components/LoginForm.vue'
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