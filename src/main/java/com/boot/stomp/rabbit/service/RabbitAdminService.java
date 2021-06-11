package com.boot.stomp.rabbit.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.javatuples.Triplet;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class RabbitAdminService {

    private final AmqpAdmin rabbitAdmin;
    private final Channel rabbitChannel;

    public void handleInfraCreationForRoutingToExchange(String sessionId) {
        Queue queue = createQueue(sessionId);
        rabbitAdmin.declareQueue(queue);
        DirectExchange exchange = new DirectExchange("stomp-prog-exchange");
        rabbitAdmin.declareExchange(exchange);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(sessionId);
        rabbitAdmin.declareBinding(binding);
    }

    public Triplet<String, String, String> handleAmqpInfraCreationForRoutingToExchangeAndTopic(String sessionId, String posId) {
        String queue = createRabbitResource("QUEUE", sessionId, posId);
        String exchange = createRabbitResource("EXCHANGE", sessionId, posId);
        try {
            AMQP.Queue.BindOk queueBind = rabbitChannel.queueBind(queue, exchange, posId+"-"+sessionId);
        } catch (IOException e) {
            log.error("Can't Bind Queue. Error Occurred.");
            throw new RuntimeException(e.getMessage());
        }
        Triplet<String, String, String> posIdSessionIdQueueTriplet = Triplet.with(posId, sessionId, queue);
        return posIdSessionIdQueueTriplet;

    }

    public Triplet<String, String, String> handleAmqpInfraCreationForRoutingToExchange(String sessionId, String posId) {
        String queue = createRabbitResource("QUEUE", sessionId, posId);
        String exchange = createRabbitResource("EXCHANGE", sessionId, posId);
        try {
            AMQP.Queue.BindOk queueBind = rabbitChannel.queueBind(queue, exchange, posId+"-"+sessionId);
        } catch (IOException e) {
            log.error("Can't Bind Queue. Error Occurred.");
            throw new RuntimeException(e.getMessage());
        }
        Triplet<String, String, String> posIdSessionIdQueueTriplet = Triplet.with(posId, sessionId, queue);
        return posIdSessionIdQueueTriplet;

    }

    private String createRabbitResource(String rabbitResource, String sessionId, String posId) {
        switch (rabbitResource) {

            case "QUEUE":
                AMQP.Queue.DeclareOk declareOk = null;
                try {
                    declareOk = rabbitChannel.queueDeclarePassive(posId + "-" + sessionId);
                } catch (IOException e) {
                    try {
                        declareOk = rabbitChannel.queueDeclare("amqp-stomp-"+posId+"-"+sessionId, false, false, true, null);
                    } catch (IOException ioException) {
                        log.error("Can't Create Queue. Error Occured.");
                        throw new RuntimeException(e.getMessage());
                    }
                }
                return declareOk.getQueue();
            case "EXCHANGE":
                AMQP.Exchange.DeclareOk exchangeDeclareOk = null;
                try {
                    exchangeDeclareOk = rabbitChannel.exchangeDeclarePassive("stomp-exchange");
                } catch (IOException e) {
                    try {
                        exchangeDeclareOk = rabbitChannel.exchangeDeclare("stomp-exchange", "stomp-exchange");
                    } catch (IOException ioException) {
                        log.error("Can't Create Queue. Error Occured.");
                        throw new RuntimeException(e.getMessage());
                    }
                }
                return "stomp-exchange";

            default:
                return null;
        }
    }


    private Queue createQueue(String sessionId) {
        //Map.of("x-dead-letter-exchange", "dead-letter-exchange")
        return new Queue("stomp-user-" + sessionId, false, false, true, new HashMap<>());
    }

}
