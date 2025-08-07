<template>
    <div class="library-container">
        <!-- 🌌 載入中遮罩 -->
        <div v-if="isLoading" class="loading-overlay">
            <div class="spinner"></div>
            <p class="loading-text">資料載入中...</p>
        </div>
        <!-- 圖書館背景裝飾 -->
        <div class="library-bg"></div>

        <!-- 主要內容區 -->
        <div class="container mt-5 position-relative">
            <!-- 標題區 -->
            <div class="header-section mb-4">
                <div
                    class="d-flex justify-content-between align-items-center p-4 bg-white rounded-lg shadow-sm border-start border-4 border-info">
                    <!-- 左側：標題 + 前往借書按鈕 -->
                    <div class="d-flex align-items-center">
                        <div class="title-section">
                            <h2 class="library-title mb-1">
                                <i class="fas fa-history text-info me-2"></i>
                                我的借閱紀錄
                            </h2>
                            <p class="text-muted small mb-0">&nbsp;&nbsp;&nbsp;My Borrowing Records</p>
                        </div>
                        <button class="btn btn-outline-primary ms-4 px-3 py-2 rounded-pill" @click="goToBorrow">
                            <i class="fas fa-book me-2"></i>前往借書&nbsp;
                        </button>
                    </div>

                    <!-- 右側：登入者 + 登出按鈕 -->
                    <div class="user-section d-flex align-items-center bg-light rounded-pill px-3 py-2">
                        <i class="fas fa-user-circle text-primary fs-5 me-2"></i>
                        <span class="fw-medium me-3">{{ userName }}</span>
                        <button class="btn btn-outline-danger btn-sm rounded-pill" @click="handleLogout">
                            <i class="fas fa-sign-out-alt me-1"></i>登出&nbsp;
                        </button>
                    </div>
                </div>
            </div>

            <!-- 成功訊息
            <div v-if="msg" class="alert alert-success alert-dismissible fade show rounded-lg shadow-sm" role="alert">
                <i class="fas fa-check-circle me-2"></i>
                {{ msg }}
                <button type="button" class="btn-close" @click="msg = ''" aria-label="Close"></button>
            </div> -->

            <!-- 借閱統計資訊 -->
            <div class="stats-section mb-4">
                <div class="row g-3">
                    <div class="col-md-4">
                        <div class="stat-card bg-gradient-info">
                            <div class="stat-icon">
                                <i class="fas fa-book-reader"></i>
                            </div>
                            <div class="stat-content">
                                <h4>{{ borrowed.length }}</h4>
                                <p>借閱中書籍</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="stat-card bg-gradient-warning">
                            <div class="stat-icon">
                                <i class="fas fa-exclamation-triangle"></i>
                            </div>
                            <div class="stat-content">
                                <h4>{{ overdueCount }}</h4>
                                <p>逾期未還</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="stat-card bg-gradient-success">
                            <div class="stat-icon">
                                <i class="fas fa-calendar-check"></i>
                            </div>
                            <div class="stat-content">
                                <h4>{{ normalCount }}</h4>
                                <p>正常借閱</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 無資料時提示 -->
            <div v-if="borrowed.length === 0" class="empty-state">
                <div class="empty-icon">
                    <i class="fas fa-book-open"></i>
                </div>
                <h4>目前沒有借閱中的書籍</h4>
                <p class="text-muted">您還沒有借閱任何書籍，快去發掘有趣的書籍吧！</p>
                <button class="btn btn-primary btn-lg rounded-pill px-4" @click="goToBorrow">
                    <i class="fas fa-book me-2"></i>前往借書&nbsp;
                </button>
            </div>

            <!-- 借閱紀錄 -->
            <div v-else class="records-section">
                <div class="section-header mb-4">
                    <h4 class="section-title">
                        <i class="fas fa-list me-2 text-info"></i>
                        借閱紀錄
                        <span class="badge bg-info ms-2">第 {{ currentPage }} 頁</span>
                    </h4>
                </div>

                <div class="row g-4">
                    <div v-for="record in paginatedBorrowed" :key="record.recordId" class="col-lg-4 col-md-6">
                        <div class="record-card h-100" :class="{ 'overdue': isOverdue(record.borrowingTime) }">
                            <div class="record-header">
                                <div class="record-status" :class="getStatusClass(record.borrowingTime)">
                                    <i :class="getStatusIcon(record.borrowingTime)" class="me-2"></i>
                                    {{ getStatusText(record.borrowingTime) }}
                                </div>
                                <div class="book-icon">
                                    <i class="fas fa-book"></i>
                                </div>
                            </div>

                            <div class="card-body d-flex flex-column">
                                <div class="book-info flex-grow-1">
                                    <h5 class="book-title">{{ record.bookName }}</h5>
                                    <p class="book-author">
                                        <i class="fas fa-user-edit me-1"></i>
                                        {{ record.author || '未知作者' }}
                                    </p>
                                    <p class="book-description">{{ record.introduction || '暫無介紹' }}</p>

                                    <div class="time-info">
                                        <div class="time-item">
                                            <i class="fas fa-calendar-plus text-primary me-2"></i>
                                            <span class="time-label">借閱時間：</span>
                                            <span class="time-value">{{ formatDateTime(record.borrowingTime) }}</span>
                                        </div>
                                        <div class="time-item">
                                            <i class="fas fa-calendar-times me-2"
                                                :class="isOverdue(record.borrowingTime) ? 'text-danger' : 'text-warning'"></i>
                                            <span class="time-label">應還時間：</span>
                                            <span class="time-value"
                                                :class="isOverdue(record.borrowingTime) ? 'text-danger fw-bold' : ''">
                                                {{ getDueDate(record.borrowingTime) }}
                                            </span>
                                        </div>
                                        <div v-if="getDaysRemaining(record.borrowingTime) !== null"
                                            class="days-remaining">
                                            <span :class="getDaysRemainingClass(record.borrowingTime)">
                                                {{ getDaysRemainingText(record.borrowingTime) }}
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <button class="return-btn mt-3" @click="handleReturn(record)">
                                    <i class="fas fa-undo me-2"></i>
                                    歸還書籍&nbsp;
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 分頁器 -->
            <nav v-if="totalPages > 1" class="mt-5">
                <div class="pagination-wrapper">
                    <ul class="pagination justify-content-center">
                        <li class="page-item" :class="{ disabled: currentPage === 1 }">
                            <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">
                                <i class="fas fa-chevron-left"></i>
                                上一頁
                            </a>
                        </li>
                        <li class="page-item" v-for="page in totalPages" :key="page"
                            :class="{ active: page === currentPage }">
                            <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
                        </li>
                        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                            <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">
                                下一頁
                                <i class="fas fa-chevron-right"></i>
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>
        </div>
        <button v-show="showScrollTop" class="scroll-to-top" @click="scrollToTop" aria-label="Scroll to Top">
            <i class="bi bi-arrow-up"></i>
        </button>
    </div>
</template>

<script setup>
import Swal from 'sweetalert2'
import { useRouter } from 'vue-router'
import { ref, onMounted, computed } from 'vue'
import { returnBook, getUserBorrowedBooks } from '@/api/borrowAndReturn'
import { fetchBooksWithInventory } from '@/api/books'

const router = useRouter()
const borrowed = ref([])
const msg = ref('')
const currentPage = ref(1)
const booksPerPage = 9
const user = JSON.parse(localStorage.getItem('user'))
const userName = ref(JSON.parse(localStorage.getItem('user'))?.userName || '未知用戶')

// 計算逾期和正常借閱數量
const overdueCount = computed(() => {
    return borrowed.value.filter(record => isOverdue(record.borrowingTime)).length
})

const normalCount = computed(() => {
    return borrowed.value.filter(record => !isOverdue(record.borrowingTime)).length
})

// 🔒 未登入者導回 login
if (!user) {
    Swal.fire({
        icon: 'warning',
        title: '請先登入',
        text: '請先登入才能查看與歸還書籍',
        confirmButtonText: '前往登入頁面',
        allowOutsideClick: false
    }).then(() => {
        router.push('/')
    })
}

// 📘 抓借書紀錄
const fetchBorrowed = async () => {
    const borrowedRes = await getUserBorrowedBooks()
    const booksRes = await fetchBooksWithInventory()

    const bookMap = new Map()
    for (const book of booksRes.data.data) {
        bookMap.set(book.bookName, {
            author: book.author,
            introduction: book.introduction
        })
    }

    borrowed.value = borrowedRes.data.data.map(record => ({
        ...record,
        author: bookMap.get(record.bookName)?.author || '',
        introduction: bookMap.get(record.bookName)?.introduction || ''
    }))
}

// ✅ 分頁資料
const paginatedBorrowed = computed(() => {
    const start = (currentPage.value - 1) * booksPerPage
    return borrowed.value.slice(start, start + booksPerPage)
})

const totalPages = computed(() => {
    return Math.ceil(borrowed.value.length / booksPerPage)
})

const changePage = (page) => {
    if (page >= 1 && page <= totalPages.value) {
        currentPage.value = page
        window.scrollTo({ top: 0, behavior: 'smooth' })
    }
}

// ✅ 還書功能
const handleReturn = async (record) => {
    const result = await Swal.fire({
        icon: 'question',
        title: '確定歸回書籍?',
        text: `您即將歸還《${record.bookName}》，確定要歸還嗎？`,
        showCancelButton: true,
        confirmButtonText: '確定',
        cancelButtonText: '取消',
        customClass: {
            confirmButton: 'btn btn-outline-primary px-4 py-2 rounded-pill me-2',
            cancelButton: 'btn btn-outline-secondary px-4 py-2 rounded-pill',
        },
        buttonsStyling: false,
    });

    if (result.isConfirmed) {
        try {
            await returnBook(record.inventoryId);
            msg.value = `已成功歸還《${record.bookName}》`;
            await fetchBorrowed();

            // ✅ 顯示打勾 + 書名提示 2 秒後自動關閉
            await Swal.fire({
                icon: 'success',
                title: '歸還成功！',
                text: `您已歸還《${record.bookName}》`,
                showConfirmButton: false,
                timer: 2000,
                timerProgressBar: true,
                toast: false,
            });

        } catch (e) {
            msg.value = e.response?.data?.message || '還書失敗';
        }
    }
};

// 時間相關功能
const getDueDate = (borrowingTime) => {
    const borrowDate = new Date(borrowingTime)
    borrowDate.setDate(borrowDate.getDate() + 14)
    return borrowDate.toLocaleString()
}

const formatDateTime = (dateTime) => {
    return new Date(dateTime).toLocaleString()
}

const isOverdue = (borrowingTime) => {
    const dueDate = new Date(borrowingTime)
    dueDate.setDate(dueDate.getDate() + 14)
    return new Date() > dueDate
}

const getDaysRemaining = (borrowingTime) => {
    const dueDate = new Date(borrowingTime)
    dueDate.setDate(dueDate.getDate() + 14)
    const today = new Date()
    const diffTime = dueDate - today
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    return diffDays
}

const getDaysRemainingText = (borrowingTime) => {
    const days = getDaysRemaining(borrowingTime)
    if (days < 0) {
        return `已逾期 ${Math.abs(days)} 天`
    } else if (days === 0) {
        return '今天到期'
    } else if (days <= 3) {
        return `還剩 ${days} 天到期`
    }
    return `還有 ${days} 天`
}

const getDaysRemainingClass = (borrowingTime) => {
    const days = getDaysRemaining(borrowingTime)
    if (days < 0) return 'text-danger fw-bold'
    if (days <= 3) return 'text-warning fw-bold'
    return 'text-success'
}

// 狀態相關功能
const getStatusClass = (borrowingTime) => {
    return isOverdue(borrowingTime) ? 'status-overdue' : 'status-normal'
}

const getStatusIcon = (borrowingTime) => {
    return isOverdue(borrowingTime) ? 'fas fa-exclamation-triangle' : 'fas fa-check-circle'
}

const getStatusText = (borrowingTime) => {
    return isOverdue(borrowingTime) ? '逾期' : '正常'
}

// 🔁 導回借書頁
const goToBorrow = () => {
    router.push('/borrow')
}

// 🔐 登出
const handleLogout = () => {
    localStorage.removeItem('user')
    localStorage.removeItem('token')
    router.push('/')
}
const isLoading = ref(!!user) // ✅ 只有登入才會顯示 loading

const showScrollTop = ref(false);

const handleScroll = () => {
    showScrollTop.value = window.scrollY > 300;
};

const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
};

onMounted(async () => {
    if (!user) return

    window.addEventListener('scroll', handleScroll) // ✅ 加上滾動事件監聽

    const start = Date.now()
    await fetchBorrowed()
    const elapsed = Date.now() - start
    const remaining = 300 - elapsed
    if (remaining > 0) {
        setTimeout(() => (isLoading.value = false), remaining)
    } else {
        isLoading.value = false
    }
})

</script>

<style scoped src="@/assets/css/return.css"></style>