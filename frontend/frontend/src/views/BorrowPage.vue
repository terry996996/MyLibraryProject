<template>
    <div class="library-container">
        <!-- 載入中遮罩 -->
        <div v-if="isLoading" class="loading-overlay">
            <div class="spinner"></div>
            <p class="loading-text">資料載入中...</p>
        </div>
        <!-- 圖書館背景 -->
        <div class="library-bg"></div>

        <!-- 主要內容 -->
        <div class="container mt-5 position-relative">
            <!-- 標題 -->
            <div class="header-section mb-4">
                <div
                    class="d-flex justify-content-between align-items-center p-4 bg-white rounded-lg shadow-sm border-start border-4 border-primary">
                    <!-- 左上角: 標題(智慧圖書館系統) & 我的借閱紀錄 -->
                    <div class="d-flex align-items-center">
                        <div class="title-section">
                            <h2 class="library-title mb-1">
                                <i class="fas fa-book-open text-primary me-2"></i>
                                智慧圖書館系統
                            </h2>
                            <p class="text-muted small mb-0">&nbsp;&nbsp; Library Management System</p>
                        </div>
                        <button class="btn btn-outline-primary ms-4 px-3 py-2 rounded-pill" @click="goToReturn">
                            <i class="fas fa-history me-2"></i>我的借閱紀錄&nbsp;
                        </button>
                    </div>

                    <!-- 右上角: 登入者[用戶名] & 登出按鈕 -->
                    <div class="user-section d-flex align-items-center bg-light rounded-pill px-3 py-2">
                        <i class="fas fa-user-circle text-primary fs-5 me-2"></i>
                        <span class="fw-medium me-3">{{ userName }}</span>
                        <button class="btn btn-outline-danger btn-sm rounded-pill" @click="handleLogout">
                            <i class="fas fa-sign-out-alt me-1"></i>登出&nbsp;
                        </button>
                    </div>
                </div>
            </div>

            <!-- 成功訊息 -->
            <!-- <div v-if="msg" class="alert alert-success alert-dismissible fade show rounded-lg shadow-sm" role="alert">
                <i class="fas fa-check-circle me-2"></i>
                {{ msg }}
                <button type="button" class="btn-close" @click="msg = ''" aria-label="Close"></button>
            </div> -->

            <!-- 書籍統計資訊 -->
            <div class="stats-section mb-4">
                <div class="row g-3">
                    <div class="col-md-6">
                        <div class="stat-card bg-gradient-primary">
                            <div class="stat-icon">
                                <i class="fas fa-books"></i>
                            </div>
                            <div class="stat-content">
                                <h4>{{ availableBookTypeCount }}</h4>
                                <p>可借書籍種類</p>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="stat-card bg-gradient-info">
                            <div class="stat-icon">
                                <i class="fas fa-bookmark"></i>
                            </div>
                            <div class="stat-content">
                                <h4>{{ borrowed.length }}</h4>
                                <p>我借閱的書籍</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 書籍列表 -->
            <div class="books-section">
                <div class="section-header mb-4">
                    <h4 class="section-title">
                        <i class="fas fa-book me-2 text-primary"></i>
                        館藏書籍
                        <span class="badge bg-primary ms-2">第 {{ currentPage }} 頁</span>
                    </h4>
                </div>

                <div class="row g-4">
                    <div v-for="(inventories, isbn) in paginatedBooks" :key="isbn" class="col-lg-4 col-md-6">
                        <div class="book-card h-100">
                            <div class="book-cover">
                                <img class="book-cover-img" :src="`/images/book-covers/${inventories[0]?.isbn}.jpg`"
                                    :alt="inventories[0]?.bookName || '書籍封面'" @error="handleImageError" />
                                <div class="book-spine"></div>
                                <div class="book-icon">
                                    <i class="fas fa-book"></i>
                                </div>
                            </div>

                            <div class="card-body d-flex flex-column">
                                <div class="book-info flex-grow-1">
                                    <h5 class="book-title">{{ inventories[0]?.bookName || '未知書名' }}</h5>
                                    <p class="book-author">
                                        <i class="fas fa-user-edit me-1"></i>
                                        {{ inventories[0]?.author || '未知作者' }}
                                    </p>
                                    <p class="book-description">&nbsp;{{ inventories[0]?.introduction || '暫無介紹' }}</p>

                                    <div class="stock-info">
                                        <span class="stock-badge" :class="getStockClass(inventories)">
                                            <i class="fas fa-warehouse me-1"></i>
                                            庫存：{{inventories.filter(i => i.status === '在庫').length}} 本&nbsp;
                                        </span>
                                    </div>
                                </div>

                                <button class="borrow-btn mt-3"
                                    :class="getBorrowButtonClass(getBorrowButtonState(inventories))"
                                    :disabled="isBorrowButtonDisabled(getBorrowButtonState(inventories))" @click="handleBorrow(
                                        inventories.find(i => i.status === '在庫')?.inventoryId,
                                        inventories[0].bookName
                                    )">
                                    <i :class="getBorrowButtonIcon(getBorrowButtonState(inventories))" class="me-2"></i>
                                    {{ getBorrowButtonText(getBorrowButtonState(inventories)) }}&nbsp;
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 分頁器 -->
            <nav v-if="totalPages > 1" class="mt-3 mb-2">
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
import { ref, computed, onMounted } from 'vue'
import { borrowBook, getUserBorrowedBooks } from '@/api/borrowAndReturn'
import { fetchBooksWithInventory } from '@/api/books'
import groupBy from 'lodash/groupBy'

const router = useRouter()
const booksRaw = ref([])
const groupedBooks = computed(() => groupBy(booksRaw.value, 'isbn'))
const borrowed = ref([])
const msg = ref('')
const user = JSON.parse(localStorage.getItem('user'))
const userName = ref(JSON.parse(localStorage.getItem('user'))?.userName || '未知用戶')

// 新增計算屬性
const availableBookTypeCount = computed(() => {
    return Object.values(groupedBooks.value).filter(
        inventories => getBorrowButtonState(inventories) === 'canBorrow'
    ).length
})

if (!user) {
    Swal.fire({
        icon: 'warning',
        title: '請先登入',
        text: '請先登入才能查看與進行書籍借閱',
        confirmButtonText: '前往登入頁面',
        allowOutsideClick: false
    }).then(() => {
        router.push('/')
    })
}

const fetchBooks = async () => {
    const res = await fetchBooksWithInventory()
    booksRaw.value = res.data.data
}

const fetchBorrowed = async () => {
    const res = await getUserBorrowedBooks()
    borrowed.value = res.data.data
}

const handleBorrow = async (inventoryId, bookName) => {
    const result = await Swal.fire({
        icon: 'question',
        title: '確定借閱書籍？',
        text: `您即將借閱《${bookName}》，確定要繼續嗎？`,
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
            await borrowBook(inventoryId);
            await fetchBooks();
            await fetchBorrowed();

            await Swal.fire({
                icon: 'success',
                title: '借閱成功！',
                text: `您已成功借閱《${bookName}》`,
                showConfirmButton: false,
                timer: 2000,
                timerProgressBar: true,
                background: '#fefefe',
            });

            msg.value = `成功借閱《${bookName}》`;
        } catch (e) {
            msg.value = e.response?.data?.message || '借書失敗';
        }
    }
};

const getBorrowButtonState = (inventories) => {
    const hasAvailable = inventories.some(i => i.status === '在庫')
    const alreadyBorrowed = borrowed.value.some(b =>
        inventories.some(i => i.bookName === b.bookName)
    )
    if (alreadyBorrowed) return 'alreadyBorrowed'
    if (!hasAvailable) return 'outOfStock'
    return 'canBorrow'
}

const getBorrowButtonText = (state) => {
    switch (state) {
        case 'alreadyBorrowed':
            return '您已借閱此書'
        case 'outOfStock':
            return '此書暫無庫存'
        default:
            return '立即借閱'
    }
}

const isBorrowButtonDisabled = (state) => {
    return state !== 'canBorrow'
}

// 新增樣式相關方法
const getBorrowButtonClass = (state) => {
    switch (state) {
        case 'alreadyBorrowed':
            return 'btn btn-warning'
        case 'outOfStock':
            return 'btn btn-secondary'
        default:
            return 'btn btn-success'
    }
}

const getBorrowButtonIcon = (state) => {
    switch (state) {
        case 'alreadyBorrowed':
            return 'fas fa-check'
        case 'outOfStock':
            return 'fas fa-times'
        default:
            return 'fas fa-download'
    }
}

const getStockClass = (inventories) => {
    const stock = inventories.filter(i => i.status === '在庫').length
    if (stock === 0) return 'stock-empty'
    if (stock <= 2) return 'stock-low'
    return 'stock-available'
}

const goToReturn = () => {
    router.push('/return')
}

const handleLogout = () => {
    localStorage.removeItem('user')
    localStorage.removeItem('token')
    router.push('/')
}

const currentPage = ref(1)
const booksPerPage = 9

const paginatedBooks = computed(() => {
    const isbns = Object.keys(groupedBooks.value)
    const start = (currentPage.value - 1) * booksPerPage
    const end = start + booksPerPage
    const selectedIsbns = isbns.slice(start, end)
    return Object.fromEntries(
        selectedIsbns.map(isbn => [isbn, groupedBooks.value[isbn]])
    )
})

const totalPages = computed(() => {
    return Math.ceil(Object.keys(groupedBooks.value).length / booksPerPage)
})

const changePage = (page) => {
    if (page >= 1 && page <= totalPages.value) {
        currentPage.value = page
        window.scrollTo({ top: 0, behavior: 'smooth' })
    }
}

const isLoading = ref(!!user) // ✅ 只有登入才會顯示 loading

const showScrollTop = ref(false);

const handleScroll = () => {
    showScrollTop.value = window.scrollY > 300;
};

const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
};

const handleImageError = (e) => {
    e.target.src = '/images/book-covers/default.jpg'
}

onMounted(async () => {
    if (!user) return;

    window.addEventListener('scroll', handleScroll);

    const start = Date.now();
    await Promise.all([fetchBooks(), fetchBorrowed()]);
    const elapsed = Date.now() - start;
    const remaining = 300 - elapsed;
    if (remaining > 0) {
        setTimeout(() => (isLoading.value = false), remaining);
    } else {
        isLoading.value = false;
    }
});

</script>

<style scoped src="@/assets/css/borrow.css"></style>