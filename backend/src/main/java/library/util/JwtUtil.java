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

    private static final String SECRET = "123456"; // 自訂密鑰，先自己寫
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 有效時間 1 天
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET); // 加密演算

    // 產生 JWT Token，用手機號碼當作產生的依據主體
    public String generateToken(String phoneNumber) {
        return JWT.create()
                .withSubject(phoneNumber)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    // 驗證 Token 並取得手機號碼
    public String validateTokenAndGetSubject(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
            // 驗證失敗回傳null
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
