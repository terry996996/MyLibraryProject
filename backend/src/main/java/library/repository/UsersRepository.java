package library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import library.domain.UsersBean;

public interface UsersRepository extends JpaRepository<UsersBean, Long> {
    // 根據手機號碼查出用戶
    @Query(value = "EXEC loginUser @phoneNumber = :phoneNumber", nativeQuery = true)
    Object loginUserRaw(@Param("phoneNumber") String phoneNumber);

    // 呼叫procedures.sql中定義的預存程序'registerUser'
    @Procedure(procedureName = "registerUser")
    void registerUser(
            @Param("phoneNumber") String phoneNumber,
            @Param("passwordHash") String passwordHash,
            @Param("passwordSalt") String passwordSalt,
            @Param("userName") String userName);

    // 呼叫procedures.sql中定義的預存程序'updateLastLogin'
    @Procedure(procedureName = "updateLastLogin")
    void updateLastLogin(@Param("userId") Long userId);
}
