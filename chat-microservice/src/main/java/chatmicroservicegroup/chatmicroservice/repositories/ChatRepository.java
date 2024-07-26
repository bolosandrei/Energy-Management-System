package chatmicroservicegroup.chatmicroservice.repositories;

import chatmicroservicegroup.chatmicroservice.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    Chat findFirstById(UUID uuid);
//    Chat findFirstByMessage(String message);
//    Chat findFirstBySender(UUID uuid);
//    Chat findFirstByReceiver(UUID uuid);
//    List<Chat> findAllBySenderAndReceiver(UUID senderUuid, UUID receiverUuid);
}
