package usermicroservicegroup.usermicroservice.security.config;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class DecodeService {
    public static Boolean isAllowed(String jwtToken, String givenRole) {
        Claims claims = JwtDecoder.decodeJwt(jwtToken);
        String role = (String) claims.get("role");
        return givenRole.equals(role);
    }
}