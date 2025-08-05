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

    public UsersBean login(LoginDTO loginRequest) {
        Object[] result;
        try {
            result = (Object[]) usersRepository.loginUserRaw(loginRequest.getPhoneNumber());
        } catch (Exception e) {
            throw new IllegalArgumentException("帳號不存在");
        }

        UsersBean user = new UsersBean();
        user.setUserId(((Number) result[0]).longValue());
        user.setPasswordHash((String) result[1]);
        user.setPasswordSalt((String) result[2]);
        user.setUserName((String) result[3]);

        String inputHash = PasswordUtil.hashPassword(loginRequest.getPassword(), user.getPasswordSalt());

        if (!inputHash.equals(user.getPasswordHash())) {
            throw new IllegalArgumentException("密碼錯誤");
        }

        usersRepository.updateLastLogin(user.getUserId());

        return user;
    }
}
