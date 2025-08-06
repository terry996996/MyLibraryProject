<template>
    <div class="container mt-5">
        <!-- 標題與按鈕 -->
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3>📘 我的借書紀錄</h3>
            <div>
                <button class="btn btn-outline-primary me-2" @click="goToBorrow">前往借書</button>
                <button class="btn btn-outline-danger" @click="handleLogout">登出</button>
            </div>
        </div>

        <p class="text-success">{{ msg }}</p>

        <!-- 無資料時提示 -->
        <div v-if="borrowed.length === 0" class="alert alert-info mt-4">目前沒有借閱中的書籍</div>

        <!-- 借閱紀錄 -->
        <div class="row" v-else>
            <div v-for="record in paginatedBorrowed" :key="record.recordId" class="col-md-4 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body d-flex flex-column justify-content-between">
                        <div>
                            <h6>{{ record.bookName }} - {{ record.author || '-' }}</h6>
                            <p class="small">{{ record.introduction || '（無介紹）' }}</p>
                            <p class="text-muted small mb-1">
                                借閱時間：{{ new Date(record.borrowingTime).toLocaleString() }}
                            </p>
                            <p class="text-muted small">
                                應還時間：{{ getDueDate(record.borrowingTime) }}
                            </p>
                        </div>
                        <button class="btn btn-success btn-sm mt-2" @click="handleReturn(record)">
                            還書
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- 分頁器 -->
        <nav v-if="totalPages > 1" class="mt-4">
            <ul class="pagination justify-content-center">
                <li class="page-item" :class="{ disabled: currentPage === 1 }">
                    <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">上一頁</a>
                </li>
                <li class="page-item" v-for="page in totalPages" :key="page" :class="{ active: currentPage === page }">
                    <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
                </li>
                <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                    <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">下一頁</a>
                </li>
            </ul>
        </nav>
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
    try {
        await returnBook(record.inventoryId)
        msg.value = `已歸還《${record.bookName}》`
        await fetchBorrowed()
    } catch (e) {
        msg.value = e.response?.data?.message || '還書失敗'
    }
}

const getDueDate = (borrowingTime) => {
    const borrowDate = new Date(borrowingTime)
    borrowDate.setDate(borrowDate.getDate() + 14)
    return borrowDate.toLocaleString()
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

onMounted(() => {
    fetchBorrowed()
})
</script>
