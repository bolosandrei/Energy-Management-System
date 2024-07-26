package monitoringmicroservicegroup.monitoringmicroservice.repo;

import monitoringmicroservicegroup.monitoringmicroservice.data.SensorInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface SensorRepository  extends JpaRepository<SensorInstance, Integer> {
    ArrayList<SensorInstance> findAllBySensorId(String sensorId);
}