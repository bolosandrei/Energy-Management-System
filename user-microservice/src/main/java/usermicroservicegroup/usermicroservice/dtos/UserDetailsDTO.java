package usermicroservicegroup.usermicroservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {

    private UUID uuid;
    @NotNull
    private String name;
    @NotNull
    private String role;
    @NotNull
    private String password;

    public UserDetailsDTO(String name, String role, String password) {
        this.name = name;
        this.role = role;
        this.password = password;
    }
}
