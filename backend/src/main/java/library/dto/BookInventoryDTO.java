package library.dto;

import java.time.LocalDateTime;

// 書籍資料的DTO類別
public class BookInventoryDTO {
    private String isbn;
    private String bookName;
    private String author;
    private String introduction;
    private Long inventoryId;
    private String status;
    private LocalDateTime storeTime;

    public BookInventoryDTO() {
    }

    public BookInventoryDTO(String isbn, String bookName, String author, String introduction,
            Long inventoryId, String status, LocalDateTime storeTime) {
        this.isbn = isbn;
        this.bookName = bookName;
        this.author = author;
        this.introduction = introduction;
        this.inventoryId = inventoryId;
        this.status = status;
        this.storeTime = storeTime;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(LocalDateTime storeTime) {
        this.storeTime = storeTime;
    }

}
