package com.boot.stomp.rabbit.interceptor;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.boot.stomp.rabbit.config.properties.StompProperties;
import com.boot.stomp.rabbit.service.JwtTokenVerifierService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.util.CollectionUtils.isEmpty;


@AllArgsConstructor
@Slf4j
public class StompFramesInterceptor implements ChannelInterceptor {

    private final StompProperties stompProperties;
    private final JwtTokenVerifierService tokenVerifierService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        final StompCommand command = headerAccessor.getCommand();
        if (command != null) {
            if(StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
                authenticateClient(headerAccessor.getNativeHeader("X-authorization"));
            }
            else if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
                log.info("SUBSCRIBE FRAME intercepted");
                List<String> destinations = headerAccessor.getNativeHeader("destination");
                if (isEmpty(destinations)) {
                    headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
                    headerAccessor.setMessage("No Destination Header present");
                    headerAccessor.setSessionId(headerAccessor.getSessionId());

                    return MessageBuilder.createMessage(message.getPayload(), headerAccessor.getMessageHeaders());
                }
                for (String subscription: destinations) {
                    updateStompDestination(subscription, headerAccessor);
                }

                headerAccessor.setNativeHeader("persistent", "false");
                headerAccessor.setHeader("persistent", false);
                return MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders());
            } else if (StompCommand.MESSAGE.equals(headerAccessor.getCommand())) {
                log.info("MESSAGE FRAME intercepted");
                updateStompDestination(headerAccessor.getNativeHeader("destination").get(0), headerAccessor);
                return MessageBuilder.createMessage(message.getPayload(), headerAccessor.getMessageHeaders());
            }
            else if (StompCommand.SEND.equals(headerAccessor.getCommand())) {
                log.info("SEND FRAME intercepted");
                /*updateStompDestinationForSendFrames(headerAccessor.getNativeHeader("destination").get(0), headerAccessor);
                return MessageBuilder.createMessage(message.getPayload(), headerAccessor.getMessageHeaders());*/
            }
        }
        return message;
    }

    private void authenticateClient(List<String> authHeaders) {
        if(!isEmpty(authHeaders)) {
            try {
                tokenVerifierService.verifyToken(authHeaders.get(0));
            } catch (JWTVerificationException exception) {
                throw new MessageDeliveryException(exception.getMessage());
            }
        } else {
            throw new MessageDeliveryException("Authentication Info Missing");
        }
    }


    private void updateStompDestination(String destination, StompHeaderAccessor headerAccessor) {
        destination = destination.replace(stompProperties.getUserDestinationPrefix(), stompProperties.getUserDestinationPrefix()+"/queue");
        String tempDestination1 = destination.substring(0, destination.indexOf("queue")+6);
        String tempDestination2 = destination.substring(destination.indexOf("queue")+6).replaceAll("/", stompProperties.getDestinationToken());
        destination = tempDestination1 + tempDestination2;
        headerAccessor.setNativeHeader("simpDestination", destination);
        headerAccessor.setNativeHeader("destination", destination);
        headerAccessor.setHeader("simpDestination", destination);
    }
    private void updateStompDestinationForSendFrames(String destination, StompHeaderAccessor headerAccessor) {
        destination = "/queue"+destination;
        String tempDestination1 = destination.substring(0, destination.indexOf("queue")+6);
        String tempDestination2 = destination.substring(destination.indexOf("queue")+6).replaceAll("/", stompProperties.getDestinationToken());
        destination = tempDestination1 + tempDestination2;
        headerAccessor.setNativeHeader("simpDestination", destination);
        headerAccessor.setNativeHeader("destination", destination);
        headerAccessor.setHeader("simpDestination", destination);
    }

}