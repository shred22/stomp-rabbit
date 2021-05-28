package com.boot.stomp.rabbit.controller;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.boot.stomp.rabbit.config.properties.BrokerProperties;
import com.boot.stomp.rabbit.model.GreetingResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.util.Objects.requireNonNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@AllArgsConstructor
public class GreetingController {

    private final SimpMessagingTemplate messagingTemplate;
    private final BrokerProperties brokerProperties;

    @MessageMapping("/hello")
   // @SendToUser("/queue/v1-greetings")
    public void handle(String greeting, SimpMessageHeaderAccessor accessor) throws JsonProcessingException {
        log.info("Stomp controller processing frames");
        ObjectMapper jsonMapper = new ObjectMapper();
        String client = jsonMapper.readTree(greeting).get("name").asText();
        messagingTemplate.convertAndSendToUser(requireNonNull(accessor.getSessionId()), brokerProperties.getRelay()+"/v1-greetings",
                GreetingResponse.builder().message("Sent with template "+client+ " ...!!").build(), createMessageHeader(accessor.getSessionId()));
       // return GreetingResponse.builder().message("Sent with template "+client+ " ...!!").build();
    }

    private MessageHeaders createMessageHeader(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}