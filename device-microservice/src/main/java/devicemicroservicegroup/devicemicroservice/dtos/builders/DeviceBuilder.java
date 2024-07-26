package devicemicroservicegroup.devicemicroservice.dtos.builders;

import devicemicroservicegroup.devicemicroservice.dtos.DeviceDTO;
import devicemicroservicegroup.devicemicroservice.dtos.DeviceDetailsDTO;
import devicemicroservicegroup.devicemicroservice.entities.Device;

public class DeviceBuilder {

    public DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Device device){
        return new DeviceDTO(device.getId(),device.getDescription(),device.getAddress(),device.getMaximumHourlyEnergyConsumption(),device.getUserId());
    }
    public static Device toEntity(DeviceDetailsDTO deviceDetailsDTO){
        return new Device(deviceDetailsDTO.getDescription(),deviceDetailsDTO.getAddress(),deviceDetailsDTO.getMaximumHourlyEnergyConsumption(),deviceDetailsDTO.getUserId());
    }
}
