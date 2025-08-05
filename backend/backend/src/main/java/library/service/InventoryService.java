// package library.service;

// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import library.domain.InventoryBean;
// import library.dto.InventoryDTO;
// import library.repository.InventoryRepository;

// @Service
// public class InventoryService {

// @Autowired
// private InventoryRepository inventoryRepository; // 依賴注入

// // 查找出全部庫存資料的方法 (會根據InventoryDTO當中指明的資料欄位，存成List)
// public List<InventoryDTO> findAllInventory() {
// return inventoryRepository.findAll().stream()
// .map(this::toResponse) // 這邊我使用方法引用的語法，讓程式碼稍微簡潔一點
// .collect(Collectors.toList());
// }

// // 根據ISBN查找出對應庫存資料的方法 (會根據InventoryDTO當中指明的資料欄位，存成List)
// public List<InventoryDTO> findByIsbn(String isbn) {
// return inventoryRepository.findByBookIsbn(isbn).stream()
// .map(this::toResponse) // 這邊我使用方法引用的語法，讓程式碼稍微簡潔一點
// .collect(Collectors.toList());
// }

// // 根據ISBN和書籍狀態查找出對應庫存資料的方法 (會根據InventoryDTO當中指明的資料欄位，存成List)
// public List<InventoryDTO> findAvailableByIsbn(String isbn) {
// return inventoryRepository.findByBookIsbnAndStatus(isbn, "在庫").stream() //
// 這邊設定給的參數為'在庫'，會根據書籍isbn找出該種書籍'在庫'的庫存資料
// .map(this::toResponse) // 這邊我使用方法引用的語法，讓程式碼稍微簡潔一點
// .collect(Collectors.toList());
// }

// //
// 定義一個toResponse方法，他會根據傳入的InventoryBean物件，new一個InventoryDTO物件，然後取得DTO中指明之欄位的資料回傳
// public InventoryDTO toResponse(InventoryBean bean) {
// return new InventoryDTO(
// bean.getInventoryId(),
// bean.getBook().getIsbn(),
// bean.getBook().getName(),
// bean.getStatus());
// }
// }
