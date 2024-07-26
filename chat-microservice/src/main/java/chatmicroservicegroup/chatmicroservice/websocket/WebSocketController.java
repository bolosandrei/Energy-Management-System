package chatmicroservicegroup.chatmicroservice.websocket;

import chatmicroservicegroup.chatmicroservice.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;


@Controller
//@CrossOrigin
//@CrossOrigin(origins = "*")
@CrossOrigin("http://localhost:5173")
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    private Message receivePublicMessage(@Payload Message message) {
        return message;
    }

    @MessageMapping("/private-message")
    private Message receivePrivateMessage(@Payload Message message) {
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
        return message;
    }
}
