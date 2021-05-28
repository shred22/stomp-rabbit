package com.boot.stomp.rabbit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.CONNECT_ACK,
        SimpMessageType.DISCONNECT, SimpMessageType.DISCONNECT_ACK,
                SimpMessageType.HEARTBEAT, SimpMessageType.UNSUBSCRIBE).permitAll()
                .simpSubscribeDestMatchers("/client/**").permitAll()
                .anyMessage().permitAll();

    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}