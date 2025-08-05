package library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import library.dto.BorrowDTO;
import library.dto.ReturnDTO;
import library.service.BorrowingService;

@RestController // @Controller + @ResponseBody
@RequestMapping("/api/borrowing") // 根源路徑
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService; // 依賴注入

    @PostMapping("/borrow")
    // 前端呼叫後端API後，對應到這個Controller，傳DTO參數進來(用@RequestBody將json格式資料轉成BorrowDTO類型的物件)
    public String borrow(@RequestBody BorrowDTO borrowRequest) {
        borrowingService.borrow(borrowRequest); // 去Service類別裡面調用借書的方法(對這個物件做一些判斷邏輯或處理)
        return "借書成功"; // 回傳結果給前端去接
    }

    @PostMapping("/return")
    // 前端呼叫後端API後，對應到這個Controller，傳DTO參數進來(用@RequestBody將json格式資料轉成ReturnDTO類型的物件)
    public String returnBook(@RequestBody ReturnDTO returnRequest) {
        borrowingService.returnBook(returnRequest); // 去Service類別裡面調用還書的方法(對這個物件做一些判斷邏輯或處理)
        return "還書成功"; // 回傳結果給前端去接
    }
}
