package devicemicroservicegroup.devicemicroservice.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "device")
public class Device implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "maximumHourlyEnergyConsumption", nullable = false)
    private float maximumHourlyEnergyConsumption;

    @Column(name = "userId", nullable = false)
    private UUID userId;

    public Device(){}

    public Device(String description, String address, float maximumHourlyEnergyConsumption, UUID uuid) {
        this.description = description;
        this.address = address;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
        this.userId = uuid;
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
