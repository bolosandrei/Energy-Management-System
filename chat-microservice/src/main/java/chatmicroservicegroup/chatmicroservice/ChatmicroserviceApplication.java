package chatmicroservicegroup.chatmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatmicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatmicroserviceApplication.class, args);
	}
	// se folosesc doar clasele Message, Status, WebConfig, WebSocketConfiguration, WebSocketController
}
