package usermicroservicegroup.usermicroservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import usermicroservicegroup.usermicroservice.services.UserService;

@SpringBootApplication
public class UserMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserMicroserviceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserService userService) {
        return args -> {
            userService.createAdmin();
        };
    }
}
