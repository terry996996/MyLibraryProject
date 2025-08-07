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

@RestController // @Controller + @ResponseBody
@RequestMapping("/api/borrowing")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    // 前端呼叫後端API後，對應到這個Controller，傳DTO參數進來(用@RequestBody將json格式資料轉成BorrowDTO類型的物件)
    @PostMapping("/borrow")
    public Map<String, Object> borrow(@RequestBody BorrowDTO borrowRequest) {
        Map<String, Object> borrowResponse = new HashMap<>();
        try {
            borrowingService.borrow(borrowRequest); // 去Service類別裡面調用借書的方法(對這個物件做一些判斷邏輯或處理)
            borrowResponse.put("success", true);
            borrowResponse.put("message", "借書成功");
        } catch (Exception e) {
            borrowResponse.put("success", false);
            borrowResponse.put("message", e.getMessage());
        }
        return borrowResponse; // 回傳結果給前端去接
    }

    // 前端呼叫後端API後，對應到這個Controller，傳DTO參數進來(用@RequestBody將json格式資料轉成ReturnDTO類型的物件)
    @PostMapping("/return")
    public Map<String, Object> returnBook(@RequestBody ReturnDTO returnRequest) {
        Map<String, Object> returnResponse = new HashMap<>();
        try {
            borrowingService.returnBook(returnRequest); // 去Service類別裡面調用還書的方法(對這個物件做一些判斷邏輯或處理)
            returnResponse.put("success", true);
            returnResponse.put("message", "還書成功");
        } catch (Exception e) {
            returnResponse.put("success", false);
            returnResponse.put("message", e.getMessage());
        }
        return returnResponse; // 回傳結果給前端去接
    }

    // 前端用GET呼叫後端API後，對應到這個Controller，後端傳資料出去給前端
    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentBorrowings() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BorrowingRecordDTO> records = borrowingService.getUserBorrowedBooks(); // 去Service類別裡面調用查找用戶借閱書籍的方法(對這個物件做一些判斷邏輯或處理)，將DTO物件包成List
            response.put("success", true);
            response.put("data", records);
            return ResponseEntity.ok(response); // 傳給前端
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系統錯誤：" + e.getMessage());
            return ResponseEntity.status(500).body(response); // 傳給前端
        }
    }
}
