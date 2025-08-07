package library.domain;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class InventoryBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;

    @Column(name = "store_time", nullable = false)
    private LocalDateTime storeTime = LocalDateTime.now();

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "inventory")
    @JsonManagedReference // 加這個Annotation防止傳資料的時候序列化遞迴循環
    private Set<BorrowingRecordBean> borrowingRecords;

    @ManyToOne
    @JoinColumn(name = "ISBN", nullable = false)
    @JsonBackReference // 加這個Annotation防止傳資料的時候序列化遞迴循環
    private BookBean book;

    // 預設書籍為在庫狀態
    @PrePersist
    public void prePersist() {
        if (status == null)
            status = "在庫";
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public LocalDateTime getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(LocalDateTime storeTime) {
        this.storeTime = storeTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<BorrowingRecordBean> getBorrowingRecords() {
        return borrowingRecords;
    }

    public void setBorrowingRecords(Set<BorrowingRecordBean> borrowingRecords) {
        this.borrowingRecords = borrowingRecords;
    }

    public BookBean getBook() {
        return book;
    }

    public void setBook(BookBean book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "InventoryBean [inventoryId=" + inventoryId + ", storeTime=" + storeTime + ", status=" + status
                + ", borrowingRecords=" + borrowingRecords + ", book=" + book + "]";
    }

}
