package usermicroservicegroup.usermicroservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import usermicroservicegroup.usermicroservice.dtos.UserDTO;
import usermicroservicegroup.usermicroservice.dtos.UserDetailsDTO;
import usermicroservicegroup.usermicroservice.dtos.builders.UserBuilder;
import usermicroservicegroup.usermicroservice.entities.User;
import usermicroservicegroup.usermicroservice.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import usermicroservicegroup.usermicroservice.security.auth.AuthenticationResponse;
import usermicroservicegroup.usermicroservice.security.config.JwtService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    private final JwtService jwtService;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    //    @Autowired
//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
    @Autowired
    public UserService(UserRepository userRepository, JwtService jwtService, RestTemplate restTemplate, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
    }


    public List<UserDTO> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserBuilder::toUserDTO).collect(Collectors.toList());
    }

    public List<UserDTO> findAllClients() {
        List<User> userList = userRepository.findAll();
        userList.removeIf(user -> !user.getRole().equals("admin"));
        return userList.stream().map(UserBuilder::toUserDTO).collect(Collectors.toList());
    }

    public UserDTO findFirstById(UUID id) {
        Optional<User> prosumerOptional = userRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        return UserBuilder.toUserDTO(prosumerOptional.get());
    }

    public UserDTO findFirstByName(String name) {
        Optional<User> prosumerOptional = Optional.ofNullable(userRepository.findFirstByName(name));
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("User with name {} was not found in db", name);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with name: " + name);
        }
        return UserBuilder.toUserDTO(prosumerOptional.get());
    }

    public UUID create(UserDetailsDTO userDetailsDTO) {
        User user = UserBuilder.toEntity(userDetailsDTO);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getId());
        return user.getId();
    }

    public UUID update(UUID uuid, UserDetailsDTO userDetailsDTO) {
        Optional<User> existingUser = userRepository.findById(uuid);
        if (existingUser.isPresent()) {
            User user = userRepository.findFirstById(uuid);
            user.setName(userDetailsDTO.getName());
            user.setRole(userDetailsDTO.getRole());
            user.setPassword(user.getPassword());
            user = userRepository.save(user);
            LOGGER.debug("User with id {} was updated in db", user.getId());
            return user.getId();
        } else {
            LOGGER.debug("User with id {} was not found in db", uuid);
            return null;
        }
    }

    public UUID delete(UUID uuid) {
        userRepository.deleteById(uuid);
        LOGGER.debug("Person with id {} was deleted from db", uuid);
        return uuid;
    }

    public Boolean loginCheck(String name, String password) {
        UserDTO userDTO = findFirstByName(name);
        if (userDTO != null && userDTO.getPassword().equals(password)) {
            System.out.println("loginCheck: True");
            return Boolean.TRUE;
        }
        System.out.println("loginCheck: False");
        return Boolean.FALSE;
    }

    public AuthenticationResponse createAdmin() {
        User user = new User("admin", "admin", passwordEncoder.encode("admin"));
        userRepository.save(user);
        User user2 = new User("client", "client", passwordEncoder.encode("client"));
        userRepository.save(user2);
        User user3 = new User("client2", "client", passwordEncoder.encode("client2"));
        userRepository.save(user3);
        var jwtToken = jwtService.generateToken(user);
        userRepository.save(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public ResponseEntity<Object[]> findAllDevicesAsAdmin() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var deviceUrl = UriComponentsBuilder.fromUriString("http://host.docker.internal:8081")
                .pathSegment("/devices/all")
                .build()
                .toUriString();

        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Object[]> responseEntity = restTemplate.exchange(deviceUrl, HttpMethod.GET, requestEntity, Object[].class);

        return responseEntity;
    }

    public UserDTO assignDeviceToUser(UUID userId, UUID deviceId) {
        User user = userRepository.findFirstById(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var deviceUrl = UriComponentsBuilder.fromUriString("http://host.docker.internal:8081")
                .pathSegment("/devices/assignDevice")
                .queryParam("userId", userId)
                .queryParam("deviceId", deviceId)
                .build()
                .toUriString();

        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);

        restTemplate.exchange(deviceUrl, HttpMethod.POST, requestEntity, Void.class);
        return UserBuilder.toUserDTO(user);
    }

    public UserDTO deassignDeviceFromUser(UUID userId, UUID deviceId) {
        User user = userRepository.findFirstById(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var deviceUrl = UriComponentsBuilder.fromUriString("http://host.docker.internal:8081")
                .pathSegment("/devices/deassignDevice")
                .queryParam("userId", userId)
                .queryParam("deviceId", deviceId)
                .build()
                .toUriString();

        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);

        restTemplate.exchange(deviceUrl, HttpMethod.PUT, requestEntity, Void.class);
        return UserBuilder.toUserDTO(user);
    }
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // Fetch user details from your database using userDetailsService
//        return userDetailsService.loadUserByUsername(username);
//    }

}
