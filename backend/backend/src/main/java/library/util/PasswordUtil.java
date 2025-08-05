package library.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

// 密碼加密的工具類(加salt並經由SHA-256演算法，加密轉成hash(雜湊值))
public class PasswordUtil {
    public static String generateSalt() {
        // 產生16字元的隨機salt值
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16]; // 存成byte陣列
        random.nextBytes(saltBytes);
        return bytesToHex(saltBytes); // 轉16進位
    }

    public static String hashPassword(String password, String salt) {
        String saltedPassword = salt + password; // 加salt後的密碼
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // 用SHA-256演算法加密
            byte[] hashBytes = md.digest(saltedPassword.getBytes()); // 加密後產生的hash值(目前是byte陣列)
            return bytesToHex(hashBytes); // 轉成16進位
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing failed", e); // 加密演算失敗
        }
    }

    private static String bytesToHex(byte[] bytes) { // 定義一個將byte陣列轉成16進位字串的靜態方法
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) // 用增強for循環抓裡面的每個元素
            sb.append(String.format("%02x", b));
        return sb.toString(); // 最後轉成String傳回去
    }
}
