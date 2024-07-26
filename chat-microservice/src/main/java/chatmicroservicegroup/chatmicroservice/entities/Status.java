package chatmicroservicegroup.chatmicroservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public enum Status {
    JOIN,
    MESSAGE,
    LEAVE
}
