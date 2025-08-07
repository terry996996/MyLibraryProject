package library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import library.domain.BorrowingRecordBean;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecordBean, Long> {

    // 呼叫procedures.sql中定義的預存程序'borrowBook'
    @Procedure(procedureName = "borrowBook")
    void borrowBook(@Param("userId") Long userId, @Param("inventoryId") Long inventoryId);

    // 呼叫procedures.sql中定義的預存程序'returnBook'
    @Procedure(procedureName = "returnBook")
    void returnBook(@Param("userId") Long userId, @Param("inventoryId") Long inventoryId);
}
