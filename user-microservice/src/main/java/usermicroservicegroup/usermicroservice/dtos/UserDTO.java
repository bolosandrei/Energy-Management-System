package usermicroservicegroup.usermicroservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private UUID id;
    private String name;
    private String role;
    private String password;
}
