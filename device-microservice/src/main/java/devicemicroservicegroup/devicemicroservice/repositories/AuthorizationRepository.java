package devicemicroservicegroup.devicemicroservice.repositories;

import devicemicroservicegroup.devicemicroservice.entities.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationRepository extends JpaRepository<JwtToken, Integer> {
    JwtToken findFirstByJwtToken(String jwtToken);
}
