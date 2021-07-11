package com.boot.stomp.rabbit.config;

import java.util.Map;

import org.javatuples.Pair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.tcp.reactor.ReactorNettyTcpClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.boot.stomp.rabbit.config.properties.BrokerProperties;
import com.boot.stomp.rabbit.config.properties.ClientProperties;
import com.boot.stomp.rabbit.config.properties.OutboundProperties;
import com.boot.stomp.rabbit.config.properties.StompProperties;
import com.boot.stomp.rabbit.interceptor.WebSocketHandshakeInterceptor;
import com.boot.stomp.rabbit.interceptor.StompFramesInterceptor;
import com.boot.stomp.rabbit.repository.DevicePayPointPairRepository;
import com.boot.stomp.rabbit.service.JwtTokenService;
import com.boot.stomp.rabbit.service.PayPointService;
import com.boot.stomp.rabbit.model.StompConnectionDetails;
import com.boot.stomp.tcp.model.DeviceConnectionDetails;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@AllArgsConstructor
@EnableWebSocketMessageBroker
public class WebStompConfig implements WebSocketMessageBrokerConfigurer {

    private final BrokerProperties brokerProperties;
    private final ClientProperties clientProperties;
    private final OutboundProperties outboundProperties;
    private final StompProperties stompProperties;
    private final ReactorNettyTcpClient<byte[]> tcpClient;
    private final JwtTokenService tokenVerifierService;
    private final PayPointService payPointService;
    private final DevicePayPointPairRepository repository;
    private final Map<String, Pair<Map<String, DeviceConnectionDetails>, Map<String, StompConnectionDetails>>> devicePayPointConnectionStatus;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp-ws/{pointId}")
                .setAllowedOrigins(stompProperties.getAllowedOrigins())
                .addInterceptors(handshakeInterceptor());
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(outboundProperties.getThreadPoolSize());
        registration.interceptors(filterChannelInterceptor());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(filterChannelInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        /**
         *  External Rabbit MQ Broker Configuration
         **/
        // Messages Going Outbound towards STOMP Client
        config.setUserDestinationPrefix(stompProperties.getUserDestinationPrefix());
        // Messages From STOMP Client which will be Forwarded to Controllers
        config.setApplicationDestinationPrefixes(stompProperties.getAppDestinationPrefix());
        // Destination Prefixes on External Broker
        config.enableStompBrokerRelay(brokerProperties.getRelay())
                .setRelayPort(brokerProperties.getPort())
                .setClientLogin(clientProperties.getLogin())
                .setClientPasscode(clientProperties.getPassword())
                .setTcpClient(tcpClient)
                .setSystemLogin(brokerProperties.getLogin())
                .setSystemPasscode(brokerProperties.getPassword());

        /**
         * Config For Enabling a Simple In-memory Broker
         * **/
        // config.setApplicationDestinationPrefixes("/app");
        //config.enableSimpleBroker("/topic", "/queue");

    }

    @Bean
    public StompFramesInterceptor filterChannelInterceptor() {
        return new StompFramesInterceptor(stompProperties, tokenVerifierService);
    }

    @Bean
    public WebSocketHandshakeInterceptor handshakeInterceptor() {
        return new WebSocketHandshakeInterceptor(payPointService, devicePayPointConnectionStatus, repository);
    }


}
