package chatmicroservicegroup.chatmicroservice.controllers;

import chatmicroservicegroup.chatmicroservice.dtos.ChatDTO;
import chatmicroservicegroup.chatmicroservice.dtos.ChatDetailsDTO;
import chatmicroservicegroup.chatmicroservice.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/chats")
public class ChatController {

    private final ChatService chatService;
    @Autowired
    public ChatController(ChatService chatService){
        this.chatService=chatService;
    }

    @GetMapping()
    public ResponseEntity<List<ChatDTO>> getAll() {
        List<ChatDTO> deviceDTOList = chatService.findAll();
        return new ResponseEntity<>(deviceDTOList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ChatDTO> getPerson(@PathVariable("id") UUID Id) {
        ChatDTO chatDTO = chatService.findFirstById(Id);
        return new ResponseEntity<>(chatDTO, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> create(@Valid @RequestBody ChatDetailsDTO chatDetailsDTO) {
        UUID uuid = chatService.create(chatDetailsDTO);
        return new ResponseEntity<>(uuid, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> update(@PathVariable("id") UUID uuidExistent, @RequestBody ChatDetailsDTO chatDetailsDTO) {
        UUID uuidUpdated = chatService.update(uuidExistent, chatDetailsDTO);
        if (uuidUpdated != null) {
            return ResponseEntity.ok(uuidUpdated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UUID> delete(@PathVariable("id") UUID uuid) {
        UUID uuidDeleted = chatService.delete(uuid);
        if (uuidDeleted != null){
            return ResponseEntity.ok(uuidDeleted);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
