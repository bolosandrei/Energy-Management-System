package devicemicroservicegroup.devicemicroservice.dtos;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class DeviceDetailsDTO {
    private UUID id;
    @NotNull
    private String description;
    @NotNull
    private String address;
    @NotNull
    private float maximumHourlyEnergyConsumption;
    @NotNull
    private UUID userId;


    public DeviceDetailsDTO() {
    }

    public DeviceDetailsDTO(UUID id, String description, String address, float maximumHourlyEnergyConsumption, UUID userId) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
        this.userId = userId;
    }

    public DeviceDetailsDTO(UUID id, String description, String address, float maximumHourlyEnergyConsumption) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
    }
    public DeviceDetailsDTO(String description, String address, float maximumHourlyEnergyConsumption) {
        this.description = description;
        this.address = address;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getMaximumHourlyEnergyConsumption() {
        return maximumHourlyEnergyConsumption;
    }

    public void setMaximumHourlyEnergyConsumption(float maximumHourlyEnergyConsumption) {
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }


}
