package devicemicroservicegroup.devicemicroservice.security;

import devicemicroservicegroup.devicemicroservice.entities.JwtToken;
import devicemicroservicegroup.devicemicroservice.repositories.AuthorizationRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final AuthorizationRepository authorizationRepository;
    private final RestTemplate restTemplate;

    public boolean isAuthenticatedAsAdmin(String jwtToken) {
        var partialPermission = this.isAuthenticatedUser(jwtToken);

        if (partialPermission) {
            Claims claims = JwtDecoder.decodeJwt(jwtToken);
            String role = (String) claims.get("role");
            return "admin".equals(role);
        }
        return false;
    }

    public boolean isAuthenticatedAsClient(String jwtToken) {
        var partialPermission = this.isAuthenticatedUser(jwtToken);

        if (partialPermission) {
            Claims claims = JwtDecoder.decodeJwt(jwtToken);
            String role = (String) claims.get("role");
            return "client".equals(role);
        }
        return false;
    }


    public boolean isAuthenticatedUser(String jwtToken) {

        if (this.isSavedToken(jwtToken)) return true;
        else {
            var permission = this.askAuthMicroserviceForPermission(jwtToken);
            if (permission.equals(HttpStatusCode.valueOf(200))) {
                this.authorizationRepository.save(JwtToken.builder().jwtToken(jwtToken).build());
                return true;
            } else return false;
        }
    }

    public boolean isSavedToken(String jwt) {
        return this.authorizationRepository.findFirstByJwtToken(jwt) != null;
    }

    public HttpStatusCode askAuthMicroserviceForPermission(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set("Authorization", "Bearer " + jwtToken);

        var userUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/spring-demo/users")
                .pathSegment("/existsUser")
                .build()
                .toUriString();

        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Boolean> response = restTemplate.exchange(userUrl, HttpMethod.GET, requestEntity, Boolean.class);
        return response.getStatusCode();
    }
}
