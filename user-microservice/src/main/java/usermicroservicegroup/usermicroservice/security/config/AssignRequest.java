package usermicroservicegroup.usermicroservice.security.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignRequest {
    @Getter
    private UUID userId;
    private UUID deviceId;
}
