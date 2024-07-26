package monitoringmicroservicegroup.monitoringmicroservice.controller;

import lombok.AllArgsConstructor;
import monitoringmicroservicegroup.monitoringmicroservice.data.SensorInstance;
import monitoringmicroservicegroup.monitoringmicroservice.service.AuthorizationService;
import monitoringmicroservicegroup.monitoringmicroservice.service.SensorInstanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
//@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class SensorInstanceController {
    private final SensorInstanceService sensorInstanceService;
    private final AuthorizationService authService;

    @GetMapping("/{sensorId}")
    public List<SensorInstance> getAllMeasurementsBySensorIdAndDate(@PathVariable String sensorId, @RequestParam String date, @RequestHeader("Authorization") String jwtToken) {
        if (this.authService.isAuthenticatedUser(jwtToken.substring(7))) {
            return sensorInstanceService.getAllValuesBySensorIdAndDate(sensorId, date);
        } else return List.of();
    }
}
