package library.domain;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UsersBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "password_salt", nullable = false)
    private String passwordSalt;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "registration_time", nullable = false)
    private LocalDateTime registrationTime = LocalDateTime.now();

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference // 加這個Annotation防止傳資料的時候序列化遞迴循環
    private Set<BorrowingRecordBean> borrowingRecords;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Set<BorrowingRecordBean> getBorrowingRecords() {
        return borrowingRecords;
    }

    public void setBorrowingRecords(Set<BorrowingRecordBean> borrowingRecords) {
        this.borrowingRecords = borrowingRecords;
    }

    @Override
    public String toString() {
        return "UsersBean [userId=" + userId + ", phoneNumber=" + phoneNumber + ", passwordHash=" + passwordHash
                + ", passwordSalt=" + passwordSalt + ", userName=" + userName + ", registrationTime=" + registrationTime
                + ", lastLoginTime=" + lastLoginTime + ", borrowingRecords=" + borrowingRecords + "]";
    }

}
