package library.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import library.domain.UsersBean;
import library.dto.BorrowDTO;
import library.dto.BorrowingRecordDTO;
import library.dto.ReturnDTO;
import library.repository.BorrowingRecordRepository;
import library.service.UsersService;

@Service
public class BorrowingService {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UsersService usersService;

    /**
     * 借書（從 JWT 自動取得 userId）
     */
    public void borrow(BorrowDTO borrowRequest) {
        Long userId = getCurrentUserId();
        borrowingRecordRepository.borrowBook(userId, borrowRequest.getInventoryId());

        System.out.println("✅ userId: " + userId + ", inventoryId: " + borrowRequest.getInventoryId());
    }

    /**
     * 還書（從 JWT 自動取得 userId）
     */
    public void returnBook(ReturnDTO returnRequest) {
        Long userId = getCurrentUserId();
        borrowingRecordRepository.returnBook(userId, returnRequest.getInventoryId());
    }

    /**
     * 查詢目前使用者尚未歸還的借書紀錄
     */
    public List<BorrowingRecordDTO> getUserBorrowedBooks() {
        Long userId = getCurrentUserId();

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("getUnreturnedBooksByUserId");
        query.registerStoredProcedureParameter("userId", Long.class, jakarta.persistence.ParameterMode.IN);
        query.setParameter("userId", userId);

        List<Object[]> results = query.getResultList();
        List<BorrowingRecordDTO> records = new ArrayList<>();

        for (Object[] row : results) {
            Long recordId = ((Number) row[0]).longValue();
            LocalDateTime borrowingTime = ((java.sql.Timestamp) row[1]).toLocalDateTime();
            String bookName = (String) row[2];
            Long inventoryId = ((Number) row[3]).longValue();

            records.add(new BorrowingRecordDTO(recordId, borrowingTime, bookName, inventoryId));
        }

        return records;
    }

    /**
     * 🔐 從 Spring Security 取得 JWT 中的使用者身分 → 查出 userId
     */
    private Long getCurrentUserId() {
        String phoneNumber = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersBean user = usersService.getUserByPhoneRaw(phoneNumber);
        return user.getUserId();
    }
}
