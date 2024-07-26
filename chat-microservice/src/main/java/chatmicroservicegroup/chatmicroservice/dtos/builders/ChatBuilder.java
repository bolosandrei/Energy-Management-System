package chatmicroservicegroup.chatmicroservice.dtos.builders;

import chatmicroservicegroup.chatmicroservice.dtos.ChatDTO;
import chatmicroservicegroup.chatmicroservice.dtos.ChatDetailsDTO;
import chatmicroservicegroup.chatmicroservice.entities.Chat;

public class ChatBuilder {
    public ChatBuilder() {
    }
    public static ChatDTO toChatDTO (Chat chat){
        return new ChatDTO(chat.getId(),chat.getMessage(),chat.getSender(),chat.getReceiver(),chat.getSeen(), chat.getDate(),chat.getStatus());
    }
    public static Chat toEntity(ChatDetailsDTO chatDetailsDTO){
        return new Chat(chatDetailsDTO.getMessage(),chatDetailsDTO.getSender(),chatDetailsDTO.getReceiver(),chatDetailsDTO.getSeen(),chatDetailsDTO.getDate(), chatDetailsDTO.getStatus());
    }
}
