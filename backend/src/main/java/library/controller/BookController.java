package library.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import library.dto.BookInventoryDTO;
import library.service.BookService;

@RestController // @Controller + @ResponseBody
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // 前端用GET呼叫後端API後，對應到這個Controller，後端傳資料出去給前端
    @GetMapping("/with-inventory")
    public ResponseEntity<Map<String, Object>> getBooksWithInventory() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookInventoryDTO> books = bookService.getBooksWithInventory(); // 去Service類別裡面調用查找館藏書籍的方法
            response.put("success", true);
            response.put("data", books);
            response.put("message", "查詢成功");
            return ResponseEntity.ok(response); // 傳給前端
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系統錯誤：" + e.getMessage());
            return ResponseEntity.status(500).body(response); // 傳給前端
        }
    }
}
