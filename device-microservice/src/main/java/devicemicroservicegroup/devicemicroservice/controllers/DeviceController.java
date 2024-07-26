package devicemicroservicegroup.devicemicroservice.controllers;

import devicemicroservicegroup.devicemicroservice.dtos.DeviceDTO;
import devicemicroservicegroup.devicemicroservice.dtos.DeviceDetailsDTO;
import devicemicroservicegroup.devicemicroservice.security.AuthorizationService;
import devicemicroservicegroup.devicemicroservice.security.JwtDecoder;
import devicemicroservicegroup.devicemicroservice.services.DeviceService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/devices")
public class DeviceController {
    private final DeviceService deviceService;
    private final AuthorizationService authorizationService;

    @Autowired
    public DeviceController(DeviceService deviceService, AuthorizationService authorizationService){this.deviceService = deviceService; this.authorizationService = authorizationService;}


    @RequestMapping(value = "/assignDevice", method = RequestMethod.POST)
    public ResponseEntity<List<DeviceDTO>> assignDevice(@RequestHeader("Authorization") String jwtToken, @RequestParam UUID userId, @RequestParam UUID deviceId) {
        if(authorizationService.isAuthenticatedUser(jwtToken))
            return ResponseEntity.ok(deviceService.assignDeviceToUser( userId,deviceId));
        else
            return ResponseEntity.status(403).body(null);
    }

    @RequestMapping(value = "/deassignDevice", method = RequestMethod.PUT)
    public ResponseEntity<List<DeviceDTO>> deassignDevice(@RequestHeader("Authorization") String jwtToken, @RequestParam UUID userId, @RequestParam UUID deviceId) {
        if(authorizationService.isAuthenticatedUser(jwtToken))
            return ResponseEntity.ok(deviceService.deassignDeviceFromUser( userId,deviceId));
        else
            return ResponseEntity.status(403).body(null);
    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getAll(
            @RequestHeader("Authorization") String jwtToken
    ) {
        String tokenConverted = jwtToken.substring(7);
        Claims claims = JwtDecoder.decodeJwt(tokenConverted);
        String role = (String) claims.get("role");
        System.out.println(role);
        if ("client".equals(role)){
            List<DeviceDTO> deviceDTOList = deviceService.findAllClient();
            return new ResponseEntity<>(deviceDTOList, HttpStatus.OK);
        } else if("admin".equals(role)){
            List<DeviceDTO> deviceDTOList = deviceService.findAllAdmin();
            return new ResponseEntity<>(deviceDTOList, HttpStatus.OK);
        } else {
            return ResponseEntity.status(403).body(null);
        }

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> getPerson(@PathVariable("id") UUID Id) {
        DeviceDTO deviceDTO = deviceService.findFirstById(Id);
        return new ResponseEntity<>(deviceDTO, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> create(@Valid @RequestBody DeviceDetailsDTO deviceDetailsDTO) {
        UUID uuid = deviceService.create(deviceDetailsDTO);
        return new ResponseEntity<>(uuid, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> update(@PathVariable("id") UUID uuidExistent, @RequestBody DeviceDetailsDTO deviceDetailsDTO) {
        UUID uuidUpdated = deviceService.update(uuidExistent, deviceDetailsDTO);
        if (uuidUpdated != null) {
            return ResponseEntity.ok(uuidUpdated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UUID> delete(@PathVariable("id") UUID uuid) {
        UUID uuidDeleted = deviceService.delete(uuid);
        if (uuidDeleted != null){
            return ResponseEntity.ok(uuidDeleted);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
