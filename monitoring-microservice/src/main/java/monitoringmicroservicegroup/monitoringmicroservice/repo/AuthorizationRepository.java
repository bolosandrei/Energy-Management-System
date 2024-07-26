package monitoringmicroservicegroup.monitoringmicroservice.repo;

import monitoringmicroservicegroup.monitoringmicroservice.data.JwtTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationRepository extends JpaRepository<JwtTokenEntity, Integer> {
    JwtTokenEntity findFirstByJwtToken(String jwtToken);
}