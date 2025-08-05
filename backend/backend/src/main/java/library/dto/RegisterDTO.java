package library.dto;

public class RegisterDTO {
    // 要傳給前端註冊頁面用的資料屬性(註冊時需要填寫、設定的基本資料)
    private String userName; // 用戶姓名(或帳號名)
    private String phoneNumber; // 手機號碼
    private String password; // 密碼

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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
