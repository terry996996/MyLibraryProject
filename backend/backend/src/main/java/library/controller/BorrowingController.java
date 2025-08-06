package library.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import library.dto.BorrowDTO;
import library.dto.BorrowingRecordDTO;
import library.dto.ReturnDTO;
import library.service.BorrowingService;

@RestController
@RequestMapping("/api/borrowing")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @PostMapping("/borrow")
    public Map<String, Object> borrow(@RequestBody BorrowDTO borrowRequest) {
        Map<String, Object> borrowResponse = new HashMap<>();
        try {
            // ✅ 使用 JWT 解出身分，不再需要從 DTO 傳 userId
            borrowingService.borrow(borrowRequest);
            borrowResponse.put("success", true);
            borrowResponse.put("message", "借書成功");
        } catch (Exception e) {
            borrowResponse.put("success", false);
            borrowResponse.put("message", e.getMessage());
        }
        return borrowResponse;
    }

    @PostMapping("/return")
    public Map<String, Object> returnBook(@RequestBody ReturnDTO returnRequest) {
        Map<String, Object> returnResponse = new HashMap<>();
        try {
            borrowingService.returnBook(returnRequest);
            returnResponse.put("success", true);
            returnResponse.put("message", "還書成功");
        } catch (Exception e) {
            returnResponse.put("success", false);
            returnResponse.put("message", e.getMessage());
        }
        return returnResponse;
    }

    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentBorrowings() {
        Map<String, Object> response = new HashMap<>();
        try {
            // ✅ 不需要手動取得 phoneNumber，service 中會自動透過 JWT 取得 userId
            List<BorrowingRecordDTO> records = borrowingService.getUserBorrowedBooks();
            response.put("success", true);
            response.put("data", records);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系統錯誤：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
