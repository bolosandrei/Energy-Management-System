package usermicroservicegroup.usermicroservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import usermicroservicegroup.usermicroservice.dtos.UserDTO;
import usermicroservicegroup.usermicroservice.dtos.UserDetailsDTO;
import usermicroservicegroup.usermicroservice.security.config.AssignRequest;
import usermicroservicegroup.usermicroservice.security.config.DecodeService;
import usermicroservicegroup.usermicroservice.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
//@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAll() {
        List<String> roles = SecurityContextHolder
                .getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        if (roles.contains("admin")
                || roles.contains("client")
        ) {
            return ResponseEntity.ok(userService.findAll());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getPerson(@PathVariable("id") UUID id) {
        UserDTO userDTO = userService.findFirstById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> create(@RequestBody UserDetailsDTO userDetailsDTO) {
        UUID uuid = userService.create(userDetailsDTO);
        return new ResponseEntity<>(uuid, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> update(@PathVariable("id") UUID uuidExistent, @RequestBody UserDetailsDTO userDetailsDTO) {
        UUID uuidUpdated = userService.update(uuidExistent, userDetailsDTO);
        if (uuidUpdated != null) {
            return ResponseEntity.ok(uuidUpdated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UUID> delete(@PathVariable("id") UUID uuid) {
        UUID uuidDeleted = userService.delete(uuid);
        if (uuidDeleted != null) {
            return ResponseEntity.ok(uuidDeleted);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @RequestMapping(value = "/assignDevice", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> assignDeviceToUser(@RequestHeader("Authorization") String authToken,
                                                      @RequestBody AssignRequest req) {
        String tokenConverted = authToken.substring(7);
        if (DecodeService.isAllowed(tokenConverted, "client")) {
            return ResponseEntity.ok(userService.assignDeviceToUser(req.getUserId(), req.getDeviceId()));
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @RequestMapping(value = "/deassignDevice", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> deassignDeviceFromUser(@RequestHeader("Authorization") String authToken,
                                                          @RequestBody AssignRequest req) {
        String tokenConverted = authToken.substring(7);
        if (DecodeService.isAllowed(tokenConverted, "client")) {
            return ResponseEntity.ok(userService.deassignDeviceFromUser(req.getUserId(), req.getDeviceId()));
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/existsUser")
    public ResponseEntity<Boolean> adevarat() {
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @RequestMapping(value = "/allDevices", method = RequestMethod.GET)
    public ResponseEntity<Object[]> findAllDevices(@RequestHeader("Authorization") String authToken) {
        System.out.println(authToken);
        String tokenConverted = authToken.substring(7);
        if (DecodeService.isAllowed(tokenConverted, "admin")) {
            return userService.findAllDevicesAsAdmin();
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @RequestMapping(value = "/allUsers", method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> findAllUsers(@RequestHeader("Authorization") String authToken) {
        String tokenConverted = authToken.substring(7);
        if (DecodeService.isAllowed(tokenConverted, "admin")) {
            return ResponseEntity.ok(userService.findAll());
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}
