package devicemicroservicegroup.devicemicroservice.repositories;

import devicemicroservicegroup.devicemicroservice.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Device findFirstById(UUID uuid);
    Device findFirstByDescription(String description);
}
