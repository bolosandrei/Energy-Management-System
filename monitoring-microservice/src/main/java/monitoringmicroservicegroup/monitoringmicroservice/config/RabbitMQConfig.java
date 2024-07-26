package monitoringmicroservicegroup.monitoringmicroservice.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.queue.json.name}")
    private String jsonQueue;

    @Value("${rabbitmq.queue.toDevice.name}")
    private String deviceQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.json.key}")
    private String routingJsonKey;

    @Value("${rabbitmq.routing.toDevices.json.key}")
    private String routingKeyToDevices;

    @Bean
    public Queue jsonQueue() { return new Queue(jsonQueue); }

    @Bean
    public Queue devicesQueue() { return new Queue(deviceQueue); }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding jsonBinding() {
        return BindingBuilder.bind(jsonQueue())
                .to(exchange())
                .with(routingJsonKey);
    }

    @Bean
    public Binding devicesBinding() {
        return BindingBuilder.bind(devicesQueue())
                .to(exchange())
                .with(routingKeyToDevices);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("host.docker.internal"); //localhost:8082
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareQueue(jsonQueue());
        admin.declareExchange(exchange());
        admin.declareBinding(jsonBinding());
        return admin;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
