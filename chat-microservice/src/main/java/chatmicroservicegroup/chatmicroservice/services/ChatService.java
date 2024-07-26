package chatmicroservicegroup.chatmicroservice.services;

import chatmicroservicegroup.chatmicroservice.dtos.ChatDTO;
import chatmicroservicegroup.chatmicroservice.dtos.ChatDetailsDTO;
import chatmicroservicegroup.chatmicroservice.dtos.builders.ChatBuilder;
import chatmicroservicegroup.chatmicroservice.entities.Chat;
import chatmicroservicegroup.chatmicroservice.repositories.ChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);
    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository){this.chatRepository=chatRepository;}

    public List<ChatDTO> findAll() {
        List<Chat> chatList = chatRepository.findAll();
        return chatList.stream().map(ChatBuilder::toChatDTO).collect(Collectors.toList());
    }

    public ChatDTO findFirstById(UUID id) {
        Optional<Chat> prosumerOptional = chatRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Chat with id {} was not found in db", id);
            throw new ResourceNotFoundException(Chat.class.getSimpleName() + " with id: " + id);
        }
        return ChatBuilder.toChatDTO(prosumerOptional.get());
    }

//    public ChatDTO findFirstByMessage(String message) {
//        Optional<Chat> prosumerOptional = Optional.ofNullable(chatRepository.findFirstByMessage(message));
//        if (!prosumerOptional.isPresent()) {
//            LOGGER.error("Chat with message {} was not found in db", message);
//            throw new ResourceNotFoundException(Chat.class.getSimpleName() + " with message: " + message);
//        }
//        return ChatBuilder.toChatDTO(prosumerOptional.get());
//    }
//
//    public ChatDTO findFirstBySender(UUID uuid) {
//        Optional<Chat> prosumerOptional = Optional.ofNullable(chatRepository.findFirstBySender(uuid));
//        if (!prosumerOptional.isPresent()) {
//            LOGGER.error("Chat with sender id {} was not found in db", uuid);
//            throw new ResourceNotFoundException(Chat.class.getSimpleName() + " with sender id: " + uuid);
//        }
//        return ChatBuilder.toChatDTO(prosumerOptional.get());
//    }
//
//    public ChatDTO findFirstByReceiver(UUID uuid) {
//        Optional<Chat> prosumerOptional = Optional.ofNullable(chatRepository.findFirstByReceiver(uuid));
//        if (!prosumerOptional.isPresent()) {
//            LOGGER.error("Chat with receiver id {} was not found in db", uuid);
//            throw new ResourceNotFoundException(Chat.class.getSimpleName() + " with receiver id: " + uuid);
//        }
//        return ChatBuilder.toChatDTO(prosumerOptional.get());
//    }

    public UUID create(ChatDetailsDTO chatDetailsDTO) {
        Chat chat = ChatBuilder.toEntity(chatDetailsDTO);
        chat = chatRepository.save(chat);
        LOGGER.debug("Chat with id {} was inserted in db", chat.getId());
        return chat.getId();
    }

    public UUID update(UUID uuid, ChatDetailsDTO chatDetailsDTO) {
        Optional<Chat> existingChat = chatRepository.findById(uuid);
        if (existingChat.isPresent()) {
            Chat chat = chatRepository.findFirstById(uuid);
            chat.setMessage(chatDetailsDTO.getMessage());
            chat.setSender(chatDetailsDTO.getSender());
            chat.setReceiver(chatDetailsDTO.getReceiver());
            chat.setSeen(chatDetailsDTO.getSeen());
            chat = chatRepository.save(chat);
            LOGGER.debug("Chat with id {} was updated in db", chat.getId());
            return chat.getId();
        } else {
            LOGGER.debug("Chat with id {} was not found in db", uuid);
            return null;
        }
    }

    public UUID delete(UUID uuid) {
        chatRepository.deleteById(uuid);
        LOGGER.debug("Chat with id {} was deleted from db", uuid);
        return uuid;
    }

    public void setSeen(UUID uuid)
    {
        Chat chat = this.chatRepository.findFirstById(uuid);
        chat.setSeen(Boolean.TRUE);
        this.chatRepository.save(chat);
    }

    public void addChatLine(String message, UUID sender,UUID receiver)
    {
        Chat chat = new Chat();
        chat.setReceiver(receiver);
        chat.setMessage(message);
        chat.setSender(sender);
        chat.setSeen(Boolean.FALSE);
        this.chatRepository.save(chat);
        System.out.println("Success");
    }

//    public List<Chat>listOfChats(UUID senderUuid, UUID receiverUuid) throws JsonProcessingException {
//        List<Chat> chats1 = new ArrayList<Chat>();
//        List<Chat> chats2 = new ArrayList<Chat>();
//        chats1 = this.chatRepository.findAllBySenderAndReceiver(senderUuid,receiverUuid);
//        chats2 = this.chatRepository.findAllBySenderAndReceiver(receiverUuid,senderUuid);
//        chats1.addAll(chats2);
//
//        Collections.sort(chats1, new Comparator<Chat>() {
//            @Override
//            public int compare(Chat c1, Chat c2) {
//                return c1.getId().compareTo(c2.getId());
//            }
//        });
//        return chats1;
//    }
}
