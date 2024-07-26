package monitoringmicroservicegroup.monitoringmicroservice.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // id ul unei masuratori
    private String sensorId; // deviceId aferent
    private Float sensorValue;
    private Date timestamp;
}
