package devicemicroservicegroup.devicemicroservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "jwts")
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String jwtToken;
}
