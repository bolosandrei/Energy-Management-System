package devicemicroservicegroup.devicemicroservice.dtos;

import java.util.Objects;
import java.util.UUID;

public class DeviceDTO {

    private UUID id;
    private String description;
    private String address;
    private float maximumHourlyEnergyConsumption;
    private UUID userId;

    public DeviceDTO(UUID id, String description, String address, float maximumHourlyEnergyConsumption, UUID userId) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
        this.userId = userId;
    }

    public DeviceDTO(){}

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

    public float getMaximumHourlyEnergyConsumption() {
        return maximumHourlyEnergyConsumption;
    }

    public void setMaximumHourlyEnergyConsumption(float maximumHourlyEnergyConsumption) {
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceDTO deviceDTO)) return false;
        return getId().equals(deviceDTO.getId()) && getDescription().equals(deviceDTO.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
