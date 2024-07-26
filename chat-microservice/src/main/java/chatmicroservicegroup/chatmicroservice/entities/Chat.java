package chatmicroservicegroup.chatmicroservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Table(name = "chat")
public class Chat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;
    @Column(name = "message", nullable = false)
    private String message;
    @Column(name = "sender", nullable = false)
    private UUID sender;
    @Column(name = "receiver", nullable = false)
    private UUID receiver;
    @Column(name = "seen", nullable = false)
    private Boolean seen = false;
    @Column(name = "date")
    private String date;
    @Column(name = "status")
    private Status status;

    public Chat(String message, UUID sender, UUID receiver, Boolean seen, String date, Status status) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.seen = seen;
        this.date = date;
        this.status = status;
    }
}
