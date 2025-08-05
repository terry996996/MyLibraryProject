// package library.controller;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import library.dto.InventoryDTO;
// import library.service.InventoryService;

// @RestController // @Controller + @ResponseBody
// @RequestMapping("/api/inventory") // 根源路徑
// public class InventoryController {

// @Autowired
// private InventoryService inventoryService; // 依賴注入

// // 供前端來呼叫後端中查找全部庫存資料的API
// // 前端會去用get呼叫後端的API，然後在這個對應的Conteoller當中，會去調inventoryService當中查全部庫存資料的方法，
// // 最後轉成json資料回傳給前端
// @GetMapping
// public List<InventoryDTO> getAllInventory() {
// return inventoryService.findAllInventory();
// }

// // 供前端來呼叫後端中透過ISBN查找該種書籍庫存資料的API
// // 前端會去用get呼叫後端的API，然後在這個對應的Conteoller當中，會去調inventoryService當中查該種書籍庫存資料的方法，
// // 最後轉成json資料回傳給前端
// @GetMapping("/isbn/{isbn}")
// public List<InventoryDTO> getInventoryByIsbn(@PathVariable String isbn) {
// return inventoryService.findByIsbn(isbn);
// }

// // 供前端來呼叫後端中透過ISBN和書籍狀態(!!!Service當中有設定會查出'在庫'的部份)查找該種書籍庫存資料的API
// //
// 前端會去用get呼叫後端的API，然後在這個對應的Conteoller當中，會去調inventoryService當中查該種書籍(在庫)庫存資料的方法，
// // 最後轉成json資料回傳給前端
// @GetMapping("/isbn/{isbn}/available")
// public List<InventoryDTO> getAvailableInventory(@PathVariable String isbn) {
// return inventoryService.findAvailableByIsbn(isbn);
// }
// }
