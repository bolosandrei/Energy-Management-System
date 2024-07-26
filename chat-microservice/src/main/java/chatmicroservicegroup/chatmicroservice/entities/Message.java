package chatmicroservicegroup.chatmicroservice.entities;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private Status status;
}

