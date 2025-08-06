package library.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // ✅ 這裡直接寫死密鑰：開發階段 OK，正式環境不建議
    private static final String SECRET = "your_super_secret_key_123456"; // ← 自訂的密鑰，請確保夠長、夠亂
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 1 天
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    /**
     * ✅ 產生 JWT Token，用 phoneNumber 當作 subject
     */
    public String generateToken(String phoneNumber) {
        return JWT.create()
                .withSubject(phoneNumber)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    /**
     * ✅ 驗證 Token 並取得 subject（phoneNumber）
     * 回傳 null 表示驗證失敗
     */
    public String validateTokenAndGetSubject(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject(); // 通常是 phoneNumber
        } catch (JWTVerificationException e) {
            System.out.println("❌ JWT 驗證失敗：" + e.getMessage()); // ✅ 加這行
            return null;
        }
    }
}
