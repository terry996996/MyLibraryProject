// package library.repository;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.data.jpa.repository.JpaRepository;

// import library.domain.InventoryBean;

// public interface InventoryRepository extends JpaRepository<InventoryBean,
// Long> {

// // 自訂一些查詢的方法(SpringDataJPA預設沒有提供的)
// // 設計一個可以透過庫存編號和書籍狀態查到對應資料的方法
// Optional<InventoryBean> findByInventoryIdAndStatus(Long inventoryId, String
// status);

// // 設計一個根據ISBN找出某種書名他所有庫存資料的方法
// List<InventoryBean> findByBookIsbn(String isbn);

// // 設計一個根據ISBN和書籍狀態找到對應庫存資料的方法
// List<InventoryBean> findByBookIsbnAndStatus(String isbn, String status);
// }
