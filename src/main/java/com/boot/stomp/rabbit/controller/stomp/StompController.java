package com.boot.stomp.rabbit.controller.stomp;

import java.io.IOException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.boot.Constants;
import com.boot.stomp.rabbit.config.properties.BrokerProperties;
import com.boot.stomp.rabbit.model.DevicePayPointPair;
import com.boot.stomp.rabbit.model.GreetingResponse;
import com.boot.stomp.rabbit.repository.DevicePayPointPairRepository;
import com.boot.stomp.tcp.server.TraditionalTcpServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.util.Objects.requireNonNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.apache.commons.collections4.MapUtils.isEmpty;

@Slf4j
@Controller
@AllArgsConstructor
public class StompController  {

    private final SimpMessagingTemplate messagingTemplate;
    private final BrokerProperties brokerProperties;
    private final RabbitTemplate rabbitTemplate;
    private final DevicePayPointPairRepository repository;
    private ApplicationContext applicationContext;

    @MessageMapping("/hello")
    // @SendToUser("/queue/v1-greetings")
    public void routeToQueue(String greeting, SimpMessageHeaderAccessor accessor) throws JsonProcessingException {
        log.info("Stomp controller processing frames");
        ObjectMapper jsonMapper = new ObjectMapper();
        String client = jsonMapper.readTree(greeting).get("name").asText();
        messagingTemplate.convertAndSendToUser(requireNonNull(accessor.getSessionId()), brokerProperties.getRelay() + "/v1-greetings",
                GreetingResponse.builder().message("Sent with template " + client + " ...!!").build(), createMessageHeader(accessor.getSessionId()));
    }

    @MessageMapping("/checkSession")
    // @SendToUser("/queue/v1-greetings")
    public void routeToQueue1(String greeting, SimpMessageHeaderAccessor accessor) throws JsonProcessingException {
        log.info("Stomp controller processing frames");
        ObjectMapper jsonMapper = new ObjectMapper();
        String client = jsonMapper.readTree(greeting).get("name").asText();
        messagingTemplate.convertAndSendToUser(requireNonNull(accessor.getSessionId()), brokerProperties.getRelay() + "/v1-greetings",
                GreetingResponse.builder().message("Sent with template " + client + " ...!!").build(), createMessageHeader(accessor.getSessionId()));
    }

    @MessageMapping("/exchmsg")
    public void routeToExchange(String greeting, SimpMessageHeaderAccessor accessor) throws IOException {
        log.info("Stomp exchange controller processing frames");
        ObjectMapper jsonMapper = new ObjectMapper();
        String client = jsonMapper.readTree(greeting).get("name").asText();
        DevicePayPointPair devicePayPointPair = repository.findByPayPointId((String) accessor.getSessionAttributes().get(Constants.PAY_POINT_ID));
        if (!isEmpty(accessor.getSessionAttributes())) {
            rabbitTemplate.convertAndSend("stomp-exchange",  devicePayPointPair.getDeviceId(), greeting+ " TCP Message .. . . . . .. . . .");
        }
        messagingTemplate.convertAndSendToUser(requireNonNull(accessor.getSessionId()), "/exchange/stomp-exchange/tQueue",
                GreetingResponse.builder().message("Sent with template " + client + " ...!!").build(), createMessageHeader(accessor.getSessionId()));
        TraditionalTcpServer tcpServer = applicationContext.getBean(TraditionalTcpServer.class);
        log.info("Sending TCP Response from Stomp Layer . . . ");
        tcpServer.sendTCPResponse(devicePayPointPair.getDeviceId());
    }

    private MessageHeaders createMessageHeader(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}