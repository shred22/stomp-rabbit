package com.boot.stomp.rabbit.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.boot.stomp.rabbit.service.RabbitAdminService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@AllArgsConstructor
public class WebSocketEventsListener {


    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = getStompHeaderAccessor(event.getMessage());
        log.info("WebSocketDisconnectListener for : "+headerAccessor.getSessionId());
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
        String sessionId = headerAccessor.getSessionId();
        /*Triplet<String, String, String> posIdSessionIdQueueTriplet = rabbitAdminService.
                handleAmqpInfraCreationForRoutingToExchange(sessionId,
                (String) headerAccessor.getSessionAttributes().get(POS_ID));
        headerAccessor.getSessionAttributes().put("queue-"+sessionId+"-"+headerAccessor.getSessionAttributes().get(POS_ID), posIdSessionIdQueueTriplet);*/
    }
    private StompHeaderAccessor getStompHeaderAccessor(Message<byte[]> message) {
        return StompHeaderAccessor.wrap(message);
    }
}