import { ref, computed } from 'vue'
import { defineStore } from 'pinia' // 這我其實沒有用到，可以刪掉或註解掉，這是pinia工具

export const useCounterStore = defineStore('counter', () => {
  const count = ref(0)
  const doubleCount = computed(() => count.value * 2)
  function increment() {
    count.value++
  }

  return { count, doubleCount, increment }
})
