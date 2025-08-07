package library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import library.domain.UsersBean;
import library.dto.LoginDTO;
import library.dto.RegisterDTO;
import library.repository.UsersRepository;
import library.util.PasswordUtil;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    // 註冊帳號：會進行加鹽跟轉雜湊，並使用預存程序來新增使用者資料
    public void register(RegisterDTO registerRequest) {
        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hashPassword(registerRequest.getPassword(), salt);

        try {
            usersRepository.registerUser(
                    registerRequest.getPhoneNumber(),
                    hash,
                    salt,
                    registerRequest.getUserName());
        } catch (Exception e) {
            throw new IllegalArgumentException("註冊失敗：" + e.getMessage());
        }
    }

    // 登入：驗證密碼後回傳完整的使用者資訊
    public UsersBean login(LoginDTO loginRequest) {
        UsersBean user = getUserByPhoneRaw(loginRequest.getPhoneNumber());

        String hashed = PasswordUtil.hashPassword(loginRequest.getPassword(), user.getPasswordSalt());
        if (!hashed.equals(user.getPasswordHash())) {
            throw new IllegalArgumentException("密碼錯誤");
        }

        // 驗證通過，會透過預存程序，更新登入時間
        usersRepository.updateLastLogin(user.getUserId());

        // 最後回傳給 Controller 做後續 JWT 處理
        return user;
    }

    // 從手機號碼查詢完整使用者資訊，供 login 和 JWT 驗證來使用
    public UsersBean getUserByPhoneRaw(String phoneNumber) {
        Object rawResult = usersRepository.loginUserRaw(phoneNumber);
        if (rawResult == null) {
            throw new IllegalArgumentException("查無此組手機號碼");
        }

        try {
            Object[] row = (Object[]) rawResult;

            UsersBean user = new UsersBean();
            user.setUserId(((Number) row[0]).longValue());
            user.setPhoneNumber((String) row[1]);
            user.setPasswordHash((String) row[2]);
            user.setPasswordSalt((String) row[3]);
            user.setUserName((String) row[4]);

            return user;
        } catch (Exception e) {
            throw new IllegalStateException("資料格式錯誤，請檢查預存程序欄位順序", e);
        }
    }
}
