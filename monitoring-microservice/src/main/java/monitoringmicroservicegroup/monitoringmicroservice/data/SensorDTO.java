package monitoringmicroservicegroup.monitoringmicroservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorDTO {
    private String sensorId;
    private Float sensorValue;
    private Date timestamp;
}
