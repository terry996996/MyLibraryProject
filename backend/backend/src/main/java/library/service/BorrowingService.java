package library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import library.dto.BorrowDTO;
import library.dto.ReturnDTO;
import library.repository.BorrowingRecordRepository;

@Service
public class BorrowingService {

    // 依賴注入
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    // 定義借書的方法 (會傳根據BorrowDTO指明儲存欄位資料的物件進來參數列表裡)
    public void borrow(BorrowDTO borrowRequest) {
        borrowingRecordRepository.borrowBook(borrowRequest.getUserId(), borrowRequest.getInventoryId()); // 呼叫借書預存程序
    }

    // 定義還書的方法 (會傳根據ReturnDTO指明儲存欄位資料的物件進來參數列表裡)
    public void returnBook(ReturnDTO returnRequest) {
        borrowingRecordRepository.returnBook(returnRequest.getUserId(), returnRequest.getInventoryId()); // 呼叫還書預存程序
    }

}
