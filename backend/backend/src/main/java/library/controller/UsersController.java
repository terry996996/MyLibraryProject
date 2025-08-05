package library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import library.domain.UsersBean;
import library.dto.LoginDTO;
import library.dto.RegisterDTO;
import library.service.UsersService;

@RestController // @Controller + @ResponseBody
@RequestMapping("/api/users") // 根源路徑
public class UsersController {

    @Autowired
    private UsersService usersService; // 依賴注入

    @PostMapping("/register")
    // 前端呼叫後端API後，對應到這個Controller，傳DTO參數進來(用@RequestBody將json格式資料轉成RegisterDTO類型的物件)
    public String register(@RequestBody RegisterDTO registerRequest) {
        usersService.register(registerRequest); // 去Service類別裡面調用註冊的方法(對這個物件做一些判斷邏輯或處理)
        return "註冊成功"; // 回傳結果給前端去接
    }

    @PostMapping("/login")
    // 前端呼叫後端API後，對應到這個Controller，傳DTO參數進來(用@RequestBody將json格式資料轉成LoginDTO類型的物件)
    public String login(@RequestBody LoginDTO loginRequest) {
        UsersBean user = usersService.login(loginRequest); // 去Service類別裡面調用註冊的方法(對這個物件做一些判斷邏輯或處理)
        return "登入成功，歡迎 " + user.getUserName(); // 回傳結果給前端去接
    }
}
