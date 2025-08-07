<template>
    <div class="login-page">
        <!-- 🌸 註冊頁專用背景動畫 -->
        <div class="animated-bg-register">
            <div class="floating-star star-a"></div>
            <div class="floating-star star-b"></div>
            <div class="floating-star star-c"></div>
            <div class="floating-star star-d"></div>
        </div>

        <!-- 🚪 註冊表單 -->
        <div class="login-card animate-pop">
            <h2 class="text-center mb-4">註冊</h2>
            <div class="login-form">
                <!-- 🙍‍♂️ 使用者名稱 -->
                <div class="mb-3">
                    <label>使用者名稱</label>
                    <input v-model="form.userName" class="form-control" @blur="onNameBlur" />
                    <div v-if="nameError" class="text-danger mt-1">{{ nameError }}</div>
                </div>

                <!-- 📱 手機號碼 -->
                <div class="mb-3">
                    <label>手機號碼</label>
                    <input v-model="form.phoneNumber" class="form-control" type="text" inputmode="numeric"
                        maxlength="10" @input="onPhoneInput" @blur="onPhoneBlur" />
                    <div v-if="phoneError" class="text-danger mt-1">{{ phoneError }}</div>
                </div>

                <!-- 🔐 密碼 -->
                <div class="mb-3">
                    <label>密碼</label>
                    <input ref="maskedPasswordInput" v-model="form.password" class="form-control" type="text"
                        autocomplete="off" maxlength="12" :placeholder="passwordPlaceholder" @focus="onPasswordFocus"
                        @input="onPasswordInput" @blur="onPasswordBlur" />
                    <div v-if="passwordError" class="text-danger mt-1">{{ passwordError }}</div>

                    <!-- 強度條 -->
                    <div v-if="form.password" class="mt-2">
                        <div class="password-strength-bar" :class="passwordStrengthClass"
                            :style="{ width: passwordStrengthWidth }"></div>
                        <small class="d-block mt-1">{{ passwordStrengthText }}</small>
                    </div>
                </div>

                <button class="btn btn-success btn-block mb-3" :disabled="!canSubmit" @click="handleRegister">
                    註冊
                </button>

                <button class="btn btn-outline-secondary btn-register" @click="goToLogin">
                    已有帳號？點我前往登入
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import Swal from 'sweetalert2'

const router = useRouter()

const form = ref({
    phoneNumber: '',
    userName: '',
    password: ''
})

const phoneError = ref('')
const nameError = ref('')
const passwordError = ref('')
const maskedPasswordInput = ref(null)
let showRealPassword = false
const touchedPhone = ref(false)
const touchedPassword = ref(false)

const passwordPlaceholder = '密碼需為 8-12 碼，英文及數字混合'

// ✅ 手機輸入：只允許數字，最多10碼，滿10碼檢查帳號
const onPhoneInput = async (e) => {
    const raw = e.target.value.replace(/\D/g, '')
    if (raw.length <= 10) {
        form.value.phoneNumber = raw
    }
    if (raw.length === 10) {
        try {
            await axios.get(`http://localhost:8080/api/users/exists/${raw}`)
            phoneError.value = '此組手機號碼已被註冊'
        } catch (_) {
            phoneError.value = ''
        }
    }
}
const onPhoneBlur = () => {
    touchedPhone.value = true
    if (form.value.phoneNumber.length < 10) {
        phoneError.value = '請輸入10碼手機號碼'
    }
}

// ✅ 名稱不得為空白
const onNameBlur = () => {
    if (form.value.userName.trim() === '') {
        nameError.value = '使用者名稱不可為空'
    } else {
        nameError.value = ''
    }
}

// ✅ 密碼限制邏輯
const isValidPassword = (pwd) => {
    return /^[A-Za-z0-9]{8,12}$/.test(pwd) &&
        /[A-Za-z]/.test(pwd) &&
        /[0-9]/.test(pwd)
}
const onPasswordFocus = () => {
    if (!showRealPassword) {
        maskedPasswordInput.value.type = 'password'
        showRealPassword = true
    }
}
const onPasswordInput = () => {
    const raw = form.value.password.replace(/\s/g, '')
    form.value.password = raw.slice(0, 12)
    if (touchedPassword.value && !isValidPassword(form.value.password)) {
        passwordError.value = '密碼需為 8-12 碼且含英文與數字'
    } else {
        passwordError.value = ''
    }
}
const onPasswordBlur = () => {
    touchedPassword.value = true
    if (!isValidPassword(form.value.password)) {
        passwordError.value = '密碼需為 8-12 碼且含英文與數字'
    } else {
        passwordError.value = ''
    }
}

// 密碼強度條
const passwordStrengthText = computed(() => {
    const pwd = form.value.password
    if (!pwd) return ''
    const hasLetter = /[A-Za-z]/.test(pwd)
    const hasNumber = /[0-9]/.test(pwd)
    if (!hasLetter || !hasNumber) return '密碼強度：弱'
    if (pwd.length < 10) return '密碼強度：中'
    return '密碼強度：強'
})

const passwordStrengthClass = computed(() => {
    const text = passwordStrengthText.value
    if (text.includes('弱')) return 'weak'
    if (text.includes('中')) return 'medium'
    if (text.includes('強')) return 'strong'
    return ''
})

const passwordStrengthWidth = computed(() => {
    const text = passwordStrengthText.value
    if (text.includes('弱')) return '33%'
    if (text.includes('中')) return '66%'
    if (text.includes('強')) return '100%'
    return '0%'
})

// ✅ 註冊按鈕是否可點
const canSubmit = computed(() => {
    return (
        form.value.phoneNumber.length === 10 &&
        form.value.userName.trim().length > 0 &&
        isValidPassword(form.value.password) &&
        phoneError.value === '' &&
        nameError.value === '' &&
        passwordError.value === ''
    )
})

// ✅ 註冊 API
const handleRegister = async () => {
    try {
        await axios.post('http://localhost:8080/api/users/register', form.value)

        let timerInterval
        let remainingSeconds = 5

        await Swal.fire({
            icon: 'success',
            title: '註冊成功',
            html: `<small>將於 <b>${remainingSeconds}</b> 秒內跳轉到登入頁面</small>`,
            timer: remainingSeconds * 1000,
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

        router.push('/')
    } catch (err) {
        Swal.fire({
            icon: 'error',
            title: '註冊失敗',
            html: `<small>${err.response?.data?.message || '發生錯誤'}</small>`,
            confirmButtonText: '重新輸入'
        })
    }
}

const goToLogin = () => {
    router.push('/')
}
</script>

<style scoped src="@/assets/css/register.css"></style>
