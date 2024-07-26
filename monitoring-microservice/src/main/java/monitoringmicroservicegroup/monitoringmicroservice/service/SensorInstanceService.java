package monitoringmicroservicegroup.monitoringmicroservice.service;

import lombok.AllArgsConstructor;
import monitoringmicroservicegroup.monitoringmicroservice.data.SensorInstance;
import monitoringmicroservicegroup.monitoringmicroservice.repo.SensorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class SensorInstanceService {
    private final SensorRepository sensorRepository;

    public List<SensorInstance> getAllValuesBySensorIdAndDate(String sensorId, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate actualDate = LocalDate.parse(date, formatter);
        List<SensorInstance> deviceInstanceArrayList =
                sensorRepository.findAllBySensorId(sensorId)
                        .stream()
                        .filter(deviceInstance -> {
                            LocalDate timeStampDate = deviceInstance.getTimestamp()
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();
                            return timeStampDate.isEqual(actualDate);
                        })
                        .toList();
        return deviceInstanceArrayList;
    }
}
