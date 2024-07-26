package monitoringmicroservicegroup.monitoringmicroservice.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "jwts")
public class JwtTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String jwtToken;
}
