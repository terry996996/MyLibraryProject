<template>
    <div class="login-page">
        <!--  動態背景層 -->
        <!-- 這邊是為了css裡面座浮動動畫的設計(四個圓圈) -->
        <div class="animated-bg">
            <div class="floating-shape shape-1"></div>
            <div class="floating-shape shape-2"></div>
            <div class="floating-shape shape-3"></div>
            <div class="floating-shape shape-4"></div>
        </div>

        <!-- 登入卡片container -->
        <div class="login-card animate-pop">
            <h2 class="text-center mb-4">登入</h2>
            <div class="login-form">
                <!-- 手機號碼 (有防呆，只能輸入數字，不能壓空白等等)-->
                <div class="mb-3">
                    <label>手機號碼</label>
                    <input v-model="form.phoneNumber" class="form-control" type="text" inputmode="numeric"
                        maxlength="10" @input="onPhoneInput" @blur="onPhoneBlur" />
                    <div v-if="phoneError" class="text-danger mt-1">{{ phoneError }}</div>
                </div>
                <!-- 手機號碼 -->

                <!-- 密碼 (有預設給用戶看的密碼規則提示，輸入框輸入後會自動消失)-->
                <!-- (有防呆，不能壓空白，輸入到不符合密碼規則是會跳提醒等等) -->
                <div class="mb-3">
                    <label>密碼</label>
                    <input ref="maskedPasswordInput" v-model="form.password" class="form-control" type="text"
                        autocomplete="off" maxlength="12" @focus="onPasswordFocus" @input="onPasswordInput"
                        @blur="onPasswordBlur" />
                    <div v-if="passwordError" class="text-danger mt-1">{{ passwordError }}</div>
                </div>
                <!-- 密碼 -->

                <!-- 登入按鈕 -->
                <button class="btn btn-success btn-block mb-3" @click="handleLogin" :disabled="!canSubmit">
                    登入
                </button>
                <!-- 登入按鈕 -->

                <!-- 前往註冊頁面的按鈕 -->
                <button class="btn btn-outline-secondary btn-register" @click="goToRegister">
                    還沒有帳號，點我前往註冊
                </button>
                <!-- 前往註冊頁面的按鈕 -->
            </div>
        </div>
    </div>
</template>


<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'

const router = useRouter()

// 屬性預設值(空字串)
const form = ref({
    phoneNumber: '',
    password: ''
})

// 屬性預設值
const phoneError = ref('')
const passwordError = ref('')
const maskedPasswordInput = ref(null)
let showRealPassword = false
const touchedPhone = ref(false)
const touchedPassword = ref(false)

// 密碼規則驗證的方法(採用正則表達式)
const isValidPassword = (pwd) => {
    return /^[A-Za-z0-9]{8,12}$/.test(pwd) &&
        /[A-Za-z]/.test(pwd) &&
        /[0-9]/.test(pwd)
}

// 手機號碼輸入
const onPhoneInput = async (e) => {
    const raw = e.target.value.replace(/\D/g, '')
    if (raw.length <= 10) {
        form.value.phoneNumber = raw
    }
    if (raw.length === 10) {
        try {
            await axios.get(`http://localhost:8080/api/users/exists/${raw}`) //呼叫後端API(根據後端傳dto來的資料進行判斷，看看有沒有這組手機帳號)
            phoneError.value = ''
        } catch (err) {
            phoneError.value = err.response?.data || '查無此組手機號碼' // 找不到帳號
        }
    }
}

// 手機號碼失焦(離開輸入框時)
const onPhoneBlur = () => {
    touchedPhone.value = true
    if (form.value.phoneNumber.length < 10) {
        phoneError.value = '請輸入10碼手機號碼' // 跳提醒
    }
}

//  密碼欄點擊後轉為 password 類型
const onPasswordFocus = () => {
    if (!showRealPassword) {
        maskedPasswordInput.value.type = 'password'
        showRealPassword = true
    }
}

//  密碼輸入，輸入到12碼會直接不讓用戶再輸入
const onPasswordInput = () => {
    const raw = form.value.password.replace(/\s/g, '')
    form.value.password = raw.slice(0, 12)
    if (touchedPassword.value && !isValidPassword(form.value.password)) {
        passwordError.value = '密碼需為 8-12 碼且含英文與數字' // 密碼的規則
    } else {
        passwordError.value = ''
    }
}

// 密碼失焦(離開輸入框時)才檢查格式
const onPasswordBlur = () => {
    touchedPassword.value = true
    if (!isValidPassword(form.value.password)) {
        passwordError.value = '密碼需為 8-12 碼且含英文與數字'
    } else {
        passwordError.value = ''
    }
}

// 登入按鈕能啟用的判斷方法
const canSubmit = computed(() => {
    return (
        form.value.phoneNumber.length === 10 &&
        isValidPassword(form.value.password) &&
        phoneError.value === '' &&
        passwordError.value === ''
    )
})

// 登入流程的方法
const handleLogin = async () => {
    const phone = form.value.phoneNumber.trim()
    const password = form.value.password.trim()

    try {
        const res = await axios.post('http://localhost:8080/api/users/login', { // 將前端用戶輸入的資料傳dto給後端，呼叫後端的api，然後到對應的controller裡面去調service裡面的一些判斷方法
            phoneNumber: phone,
            password: password
        })

        if (res.data.success) {
            const userName = res.data.data.userName || '用戶' // 下面將登入資料存localStorage
            localStorage.setItem('user', JSON.stringify(res.data.data))
            localStorage.setItem('token', res.data.token)

            // SweetAlert + 倒數跳轉
            let timerInterval
            let remainingSeconds = 5 // 5秒後跳轉到借書頁面
            // 登入成功
            await Swal.fire({
                icon: 'success',
                title: `登入成功，歡迎 ${userName}`,
                html: `<small>將於 <b>${remainingSeconds}</b> 秒內跳轉到本圖書館借閱系統</small>`,
                timer: remainingSeconds * 1000, // 會動態更新倒數時間
                showConfirmButton: false,
                didOpen: () => {
                    const b = Swal.getHtmlContainer().querySelector('b')
                    timerInterval = setInterval(() => {
                        remainingSeconds--
                        if (b) b.textContent = remainingSeconds.toString()
                    }, 1000)
                },
                willClose: () => {
                    clearInterval(timerInterval)
                }
            })

            router.push('/borrow') // 導往借書頁
        }
        // 登入失敗，跳的提示框
    } catch (err) {
        Swal.fire({
            icon: 'error',
            title: '登入失敗',
            html: '<small>手機號碼或密碼錯誤</small>',
            confirmButtonText: '重新輸入'
        })
    }
}

// 導往註冊頁面的方法
const goToRegister = () => {
    router.push('/register')
}
</script>

<!-- 外掛樣式 -->
<style scoped src="@/assets/css/login.css"></style>
