<template>
    <div class="container mt-5">
        <!-- 標題區 -->
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3>📚 借書系統</h3>
            <div>
                <button class="btn btn-outline-primary me-2" @click="goToReturn">我的借閱紀錄</button>
                <button class="btn btn-outline-danger" @click="handleLogout">登出</button>
            </div>
        </div>

        <p class="text-success">{{ msg }}</p>

        <!-- 書籍列表 -->
        <div class="row">
            <div v-for="(inventories, isbn) in paginatedBooks" :key="isbn" class="col-md-4 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body d-flex flex-column justify-content-between">
                        <div>
                            <h6>{{ inventories[0]?.bookName || '-' }} - {{ inventories[0]?.author || '-' }}</h6>
                            <p class="small">{{ inventories[0]?.introduction || '（無介紹）' }}</p>
                            <p class="text-muted">可借庫存數：{{inventories.filter(i => i.status === '在庫').length}}</p>
                        </div>
                        <button class="btn btn-primary btn-sm mt-2"
                            :disabled="isBorrowButtonDisabled(getBorrowButtonState(inventories))" @click="handleBorrow(
                                inventories.find(i => i.status === '在庫')?.inventoryId,
                                inventories[0].bookName
                            )">
                            {{ getBorrowButtonText(getBorrowButtonState(inventories)) }}
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
                <li class="page-item" v-for="page in totalPages" :key="page" :class="{ active: page === currentPage }">
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
    try {
        await borrowBook(inventoryId)
        msg.value = `成功借閱《${bookName}》`
        await fetchBooks()
        await fetchBorrowed()
    } catch (e) {
        msg.value = e.response?.data?.message || '借書失敗'
    }
}

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
            return '您已借過此書，無法重複借閱'
        case 'outOfStock':
            return '尚無庫存，無法借閱'
        default:
            return '可借閱'
    }
}

const isBorrowButtonDisabled = (state) => {
    return state !== 'canBorrow'
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


// 分頁顯示的書籍（每頁 9 組 isbn）
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

onMounted(() => {
    fetchBooks()
    fetchBorrowed()
})
</script>
