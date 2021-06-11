package com.boot.stomp.rabbit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;

import com.boot.stomp.rabbit.config.properties.StompProperties;
import com.boot.stomp.rabbit.resolver.RabbitDestinationResolver;

/*@AllArgsConstructor
@Configuration
@Slf4j*/
public class WebStompDestinationConfig extends WebSocketMessageBrokerConfigurationSupport {

    private final StompProperties properties;

    public WebStompDestinationConfig(StompProperties properties) {
        this.properties = properties;
    }

    @Bean
    @Override
    public UserDestinationResolver userDestinationResolver(
            SimpUserRegistry userRegistry, AbstractSubscribableChannel clientInboundChannel,
            AbstractSubscribableChannel clientOutboundChannel) {
        RabbitDestinationResolver resolver = new RabbitDestinationResolver(userRegistry);
        String prefix = properties.getUserDestinationPrefix();
        if (prefix != null) {
            resolver.setUserDestinationPrefix(prefix);
        }
        return resolver;
    }


    @Override
    protected void registerStompEndpoints(StompEndpointRegistry registry) {

    }
}
