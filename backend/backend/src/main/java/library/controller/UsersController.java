package library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import library.domain.UsersBean;
import library.dto.ApiResponse;
import library.dto.LoginDTO;
import library.dto.RegisterDTO;
import library.dto.UserDTO;
import library.service.UsersService;
import library.util.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterDTO registerRequest) {
        try {
            usersService.register(registerRequest);
            return ResponseEntity.ok(new ApiResponse(true, "註冊成功"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "系統錯誤"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginDTO loginRequest) {
        try {
            // 登入驗證（密碼加鹽雜湊比對）
            UsersBean user = usersService.login(loginRequest);

            // 產生 JWT token（以 phoneNumber 當 subject）
            String token = jwtUtil.generateToken(user.getPhoneNumber());

            // 回傳 DTO（可擴充）
            UserDTO userDto = new UserDTO(user.getUserId(), user.getUserName());

            // 建立回傳資料物件（含 JWT）
            ApiResponse response = new ApiResponse(true, "登入成功", userDto);
            response.setToken(token); // ← 你 ApiResponse 裡記得加上 `token` 欄位

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "系統錯誤"));
        }
    }

    @GetMapping("/exists/{phone}")
    public ResponseEntity<?> checkPhoneExists(@PathVariable String phone) {
        try {
            usersService.getUserByPhoneRaw(phone);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("查無此組手機號碼");
        }
    }
}
