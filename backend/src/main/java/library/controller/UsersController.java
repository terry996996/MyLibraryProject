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

@RestController // @Controller + @ResponseBody
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtUtil jwtUtil;

    // 前端呼叫後端API後，對應到這個Controller，傳DTO參數進來(用@RequestBody將json格式資料轉成RegisterDTO類型的物件)
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterDTO registerRequest) {
        try {
            usersService.register(registerRequest); // 去Service類別裡面調用註冊的方法(對這個物件做一些判斷邏輯或處理)
            return ResponseEntity.ok(new ApiResponse(true, "註冊成功")); // 回傳結果給前端去接
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, e.getMessage())); // 回傳結果給前端去接
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "系統錯誤")); // 回傳結果給前端去接
        }
    }

    // 前端呼叫後端API後，對應到這個Controller，傳DTO參數進來(用@RequestBody將json格式資料轉成LoginDTO類型的物件)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginDTO loginRequest) {
        try {
            // 登入驗證
            UsersBean user = usersService.login(loginRequest); // 去Service類別裡面調用登入的方法(對這個物件做一些判斷邏輯或處理)

            // 產生 JWT token
            String token = jwtUtil.generateToken(user.getPhoneNumber());

            UserDTO userDto = new UserDTO(user.getUserId(), user.getUserName());

            ApiResponse response = new ApiResponse(true, "登入成功", userDto);
            response.setToken(token);

            return ResponseEntity.ok(response); // 回傳結果給前端去接

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, e.getMessage())); // 回傳結果給前端去接
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "系統錯誤")); // 回傳結果給前端去接
        }
    }

    // 前端用GET呼叫後端API後，對應到這個Controller，後端傳資料出去給前端
    @GetMapping("/exists/{phone}")
    public ResponseEntity<?> checkPhoneExists(@PathVariable String phone) {
        try {
            usersService.getUserByPhoneRaw(phone); // 去Service類別裡面調用以手機號碼查找用戶的方法
            return ResponseEntity.ok().build(); // 傳給前端
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("查無此組手機號碼"); // 傳給前端
        }
    }
}
