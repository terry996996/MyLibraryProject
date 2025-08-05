package library.dto;

public class LoginDTO {
    // 要傳給前端登入頁面用的資料屬性(透過手機號碼登入)
    private String phoneNumber; // 手機號碼
    private String password; // 密碼

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
