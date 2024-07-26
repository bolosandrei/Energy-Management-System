package monitoringmicroservicegroup.monitoringmicroservice.consumer;

import lombok.RequiredArgsConstructor;
import monitoringmicroservicegroup.monitoringmicroservice.data.SensorDTO;
import monitoringmicroservicegroup.monitoringmicroservice.data.SensorInstance;
import monitoringmicroservicegroup.monitoringmicroservice.repo.SensorRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import java.util.ArrayList;
@Service
@RequiredArgsConstructor
public class QueueMediator {

    // Broker
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueMediator.class);
    private final SensorRepository sensorRepository;
    private final AmqpTemplate amqpTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.toDevices.json.key}")
    private String routingKeyToDevices;

    @RabbitListener(queues = "${rabbitmq.queue.json.name}")
    public void recalculateHourlyConsumption(SensorDTO sensorDto) {
        LOGGER.info(String.format("Message received -> %s", sensorDto));

        SensorInstance sensorInstance = SensorInstance.builder()
                .sensorId(sensorDto.getSensorId())
                .sensorValue(sensorDto.getSensorValue())
                .timestamp(sensorDto.getTimestamp())
                .build();
        this.sensorRepository.save(sensorInstance);

        Float hourlyConsumption = recalculatePerHour(sensorDto);
        SensorDTO newSensorDto = SensorDTO.builder()
                .sensorId(sensorDto.getSensorId())
                .sensorValue(hourlyConsumption)
                .timestamp(sensorDto.getTimestamp())
                .build();
        resendHourlyConsumption(newSensorDto);
        LOGGER.info(String.format("Sending message to devices :%s", newSensorDto));
    }

    //actualizare masurat
    private Float recalculatePerHour(SensorDTO sensorDto) {
        ArrayList<SensorInstance> inputsForCurrentDevice = this.sensorRepository.findAllBySensorId(sensorDto.getSensorId());
        Float sum = inputsForCurrentDevice.stream()
                .filter(device -> device.getTimestamp().getHours() == sensorDto.getTimestamp().getHours())
                .map(SensorInstance::getSensorValue)
                .reduce((float) 0, Float::sum);

        LOGGER.info(String.format("New hourly consumption for device: -> %s: %f", sensorDto, sum));
        return sum;
    }

    private void resendHourlyConsumption(SensorDTO sensorDto) {
        amqpTemplate.convertAndSend(exchange, routingKeyToDevices, sensorDto);
    }
}
