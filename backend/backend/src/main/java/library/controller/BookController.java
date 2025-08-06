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

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/with-inventory")
    public ResponseEntity<Map<String, Object>> getBooksWithInventory() {
        Map<String, Object> response = new HashMap<>();
        try {
            // ⬇️ 如果你想記錄誰在查，也可以從 JWT 取出 phoneNumber
            // String phoneNumber = (String)
            // SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<BookInventoryDTO> books = bookService.getBooksWithInventory();

            response.put("success", true);
            response.put("data", books);
            response.put("message", "查詢成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系統錯誤：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
