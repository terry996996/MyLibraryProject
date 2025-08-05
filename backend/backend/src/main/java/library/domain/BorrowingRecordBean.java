package library.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "borrowing_record")
public class BorrowingRecordBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "borrowing_time", nullable = false)
    private LocalDateTime borrowingTime = LocalDateTime.now();

    @Column(name = "return_time")
    private LocalDateTime returnTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // 加這個Annotation防止傳資料的時候序列化遞迴循環
    private UsersBean user;

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    @JsonBackReference // 加這個Annotation防止傳資料的時候序列化遞迴循環
    private InventoryBean inventory;

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

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }

    public UsersBean getUser() {
        return user;
    }

    public void setUser(UsersBean user) {
        this.user = user;
    }

    public InventoryBean getInventory() {
        return inventory;
    }

    public void setInventory(InventoryBean inventory) {
        this.inventory = inventory;
    }

    @Override
    public String toString() {
        return "BorrowingRecordBean [recordId=" + recordId + ", borrowingTime=" + borrowingTime + ", returnTime="
                + returnTime + ", user=" + user + ", inventory=" + inventory + "]";
    }

}
