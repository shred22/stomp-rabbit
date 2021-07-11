package com.boot.stomp.rabbit.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RabbitMqReceiver implements RabbitListenerConfigurer {

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
        log.info("RabbitListenerEndpointRegistrar " );
    }
    /*@RabbitListener(queues = {"amqp-shared-queue", "amqp-common-topic"})
    public void receivedMessage(String message) {
        log.info("User Details Received is.. " + message);
    }*/
}