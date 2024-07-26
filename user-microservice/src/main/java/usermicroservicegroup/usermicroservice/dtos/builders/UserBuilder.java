package usermicroservicegroup.usermicroservice.dtos.builders;

import usermicroservicegroup.usermicroservice.dtos.UserDTO;
import usermicroservicegroup.usermicroservice.dtos.UserDetailsDTO;
import usermicroservicegroup.usermicroservice.entities.User;

public class UserBuilder {

    public UserBuilder() {
    }
    public static UserDTO toUserDTO (User user){
        return new UserDTO(user.getId(), user.getName(),user.getRole(),user.getPassword());
    }

    public static User toEntity(UserDetailsDTO userDetailsDTO){
        return new User(userDetailsDTO.getName(),userDetailsDTO.getRole(),userDetailsDTO.getPassword());
    }
}
