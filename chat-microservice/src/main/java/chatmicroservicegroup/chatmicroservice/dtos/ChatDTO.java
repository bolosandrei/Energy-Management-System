package chatmicroservicegroup.chatmicroservice.dtos;

import chatmicroservicegroup.chatmicroservice.entities.Status;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatDTO {
    private UUID id;
    private String message;
    private UUID sender;
    private UUID receiver;
    private Boolean seen;
    private String date;
    private Status status;
}
