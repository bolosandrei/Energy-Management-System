package chatmicroservicegroup.chatmicroservice.dtos;

import chatmicroservicegroup.chatmicroservice.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatDetailsDTO {
    private UUID id;
    @NotNull
    private String message;
    @NotNull
    private UUID sender;
    @NotNull
    private UUID receiver;
    @NotNull
    private Boolean seen;
    private String date;
    private Status status;

    public ChatDetailsDTO(String message, UUID sender, UUID receiver, Boolean seen, String date, Status status) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.seen = seen;
        this.date = date;
        this.status = status;
    }
}
