package com.boot.stomp.rabbit.error;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.ControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@ControllerAdvice
public class WebSocketExceptionHandler {

    private final SimpUserRegistry userRegistry;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageExceptionHandler(InvalidTokenException.class)
    public void handleException(InvalidTokenException e) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        headerAccessor.setMessage(e.getMessage());
        headerAccessor.setSessionId("ses");
        headerAccessor.setLeaveMutable(true);
        messagingTemplate.convertAndSendToUser("ses", "/client/error", new byte[0], headerAccessor.getMessageHeaders());
    }

    @MessageExceptionHandler
    @SendToUser("/queue/v1-error")
    public String handleException(RuntimeException ex, SimpMessageHeaderAccessor headerAccessor) {
        return ex.getMessage();
    }
}