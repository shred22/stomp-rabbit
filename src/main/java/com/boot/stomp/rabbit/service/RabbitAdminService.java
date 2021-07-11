package com.boot.stomp.rabbit.service;

import java.io.IOException;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

import static java.util.Map.of;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.amqp.core.BindingBuilder.bind;

@Service
@Slf4j
public class RabbitAdminService {

    private final Channel rabbitChannel;
    private final AmqpAdmin rabbitAdmin;
    private final RabbitTemplate rabbitTemplate;


    public RabbitAdminService(Channel rabbitChannel, AmqpAdmin rabbitAdmin, RabbitTemplate rabbitTemplate) {
        this.rabbitChannel = rabbitChannel;
        this.rabbitAdmin = rabbitAdmin;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Boolean handleAmqpInfraCreationForRoutingToExchange(String exchangeName, String exchangeType, String queueName,
                                                               String deviceId, MessageListener messageListener) {
        addExchange(exchangeName, exchangeType);
        addQueue(queueName, deviceId, messageListener);
        ///addBinding(new DirectExchange(exchangeName), queue, deviceId);
        //rabbitChannel.queueBind(queue, exchange, deviceId);
        return true;
    }

    private void addBinding(Exchange exchange, Queue queue, String routingKey) {
        rabbitAdmin.declareBinding(bind(queue).to(exchange).with(routingKey).noargs());
    }

    private void addExchange(String exchangeName, String exchangeType) {
        try {
            rabbitChannel.exchangeDeclarePassive(exchangeName);
        } catch (IOException e) {
            try {
                log.error("Rabbit Exchange doesn't exist. Creating new Exchange ...");
                rabbitChannel.exchangeDeclare(exchangeName, exchangeType);
            } catch (IOException ioException) {
                log.error("Can't Create Exchange. Error Occurred.");
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private Queue addQueue(String queueName, String deviceId, MessageListener messageListener) {


        try {
            rabbitChannel.queueDeclare(queueName, true, false,false, of("x-queue-type", "quorum"));
            rabbitChannel.queueBind(queueName, "stomp-exchange", deviceId);
            addQueueToListener(queueName, deviceId, messageListener);
        } catch (IOException e) {
            log.error("Error while Creating Queue .. .. .. .......:"+e.getMessage());

        }


        return null;

    }


    public void addQueueToListener(String queue, String deviceId, MessageListener messageListener) {
        AbstractMessageListenerContainer listener = startListening(queue, messageListener);
    }

    public AbstractMessageListenerContainer startListening(String queue, MessageListener messageListener) {
        log.info("Adding Rabbit Queue  Listener for Queue. . .  . . . : "+queue);
        SimpleMessageListenerContainer  listener = new SimpleMessageListenerContainer(rabbitTemplate.getConnectionFactory());
        /*rabbitAdmin.declareBinding(bind(queue).to(exchange).with(key).noargs());
        listener.addQueues(queue);*/
        listener.addQueueNames(queue);
        listener.setMessageListener(messageListener);
        listener.start();
        return listener;
    }
}
