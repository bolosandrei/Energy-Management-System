package devicemicroservicegroup.devicemicroservice.services;

import devicemicroservicegroup.devicemicroservice.dtos.DeviceDTO;
import devicemicroservicegroup.devicemicroservice.dtos.DeviceDetailsDTO;
import devicemicroservicegroup.devicemicroservice.dtos.builders.DeviceBuilder;
import devicemicroservicegroup.devicemicroservice.entities.Device;
import devicemicroservicegroup.devicemicroservice.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository){this.deviceRepository=deviceRepository;}

    public List<DeviceDTO> findAll() {
        List<Device> userList = deviceRepository.findAll();
        return userList.stream().map(DeviceBuilder::toDeviceDTO).collect(Collectors.toList());
    }
    public List<DeviceDTO> findAllClient() {
        List<Device> userList = deviceRepository.findAll();
        return userList.stream().map(DeviceBuilder::toDeviceDTO).collect(Collectors.toList());
    }
    public List<DeviceDTO> findAllAdmin() {
        List<Device> userList = deviceRepository.findAll();
        return userList.stream().map(DeviceBuilder::toDeviceDTO).collect(Collectors.toList());
    }

    public DeviceDTO findFirstById(UUID id) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDTO(prosumerOptional.get());
    }

    public UUID create(DeviceDetailsDTO deviceDetailsDTO) {
        Device device = DeviceBuilder.toEntity(deviceDetailsDTO);
        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());
        return device.getId();
    }


    public UUID update(UUID uuid, DeviceDetailsDTO deviceDetailsDTO) {
        Optional<Device> existingDevice = deviceRepository.findById(uuid);
        if (existingDevice.isPresent()) {
            Device device = deviceRepository.findFirstById(uuid);
            device.setDescription(deviceDetailsDTO.getDescription());
            device.setAddress(deviceDetailsDTO.getAddress());
            device.setMaximumHourlyEnergyConsumption(deviceDetailsDTO.getMaximumHourlyEnergyConsumption());
            device.setUserId(deviceDetailsDTO.getUserId());
            device = deviceRepository.save(device);
            LOGGER.debug("Device with id {} was inserted in db", device.getId());
            return device.getId();
        } else {
            LOGGER.debug("Device with id {} was not found in db", uuid);
            return null;
        }
    }

    public UUID delete(UUID uuid) {
        deviceRepository.deleteById(uuid);
        LOGGER.debug("Device with id {} was deleted from db", uuid);
        return uuid;
    }

    public List<DeviceDTO> assignDeviceToUser(UUID userId, UUID deviceId) {
        Device device = deviceRepository.findFirstById(deviceId);
        UUID deviceUsers = device.getUserId();
        if (deviceUsers != null) {
//            deviceUsers.remove(userId);
//            device.setIdUser(deviceUsers);
//            deviceRepo.save(device);
        }
//        return DeviceBuilder.toDeviceDTO(device);
        List<Device> userList = deviceRepository.findAll();
        return userList.stream().map(DeviceBuilder::toDeviceDTO).collect(Collectors.toList());
    }
    public List<DeviceDTO> deassignDeviceFromUser(UUID userId, UUID deviceId) {
        Device device = deviceRepository.findFirstById(deviceId);
        UUID deviceUsers = device.getUserId();
        if (deviceUsers != null) {
//            deviceUsers.remove(userId);
//            device.setIdUser(deviceUsers);
//            deviceRepo.save(device);
        }
//        return DeviceBuilder.toDeviceDTO(device);
        List<Device> userList = deviceRepository.findAll();
        return userList.stream().map(DeviceBuilder::toDeviceDTO).collect(Collectors.toList());
    }
}
