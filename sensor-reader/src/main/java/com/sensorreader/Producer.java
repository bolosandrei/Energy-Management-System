package com.sensorreader;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Producer {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.json.key}")
    private String routingJsonKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
    private final AmqpTemplate amqpTemplate;

    public void sendJsonMessage(SensorDTO sensorDTO) {
        LOGGER.info(String.format("Json message sent -> %s", sensorDTO.toString()));
        amqpTemplate.convertAndSend(exchange, routingJsonKey, sensorDTO);
    }
}
