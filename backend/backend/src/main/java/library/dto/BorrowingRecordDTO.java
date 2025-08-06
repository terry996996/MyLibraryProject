package library.dto;

import java.time.LocalDateTime;

public class BorrowingRecordDTO {
    private Long recordId;
    private LocalDateTime borrowingTime;
    private String bookName;
    private Long inventoryId;

    public BorrowingRecordDTO(Long recordId, LocalDateTime borrowingTime, String bookName, Long inventoryId) {
        this.recordId = recordId;
        this.borrowingTime = borrowingTime;
        this.bookName = bookName;
        this.inventoryId = inventoryId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public LocalDateTime getBorrowingTime() {
        return borrowingTime;
    }

    public void setBorrowingTime(LocalDateTime borrowingTime) {
        this.borrowingTime = borrowingTime;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

}
