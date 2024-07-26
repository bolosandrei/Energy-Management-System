package monitoringmicroservicegroup.monitoringmicroservice.data;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtDecoder {

    private static final String SECRET_KEY="aa415bcf7e0058044f406d70d15340e9db2505316bcaef2f00ff4b46b76b992a";
    public static Claims decodeJwt(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
