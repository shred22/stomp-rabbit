package com.boot.stomp.tcp.config;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.tcp.serializer.TcpCodecs;

import com.boot.stomp.tcp.config.properties.TcpServerProperties;
import com.boot.stomp.tcp.transformer.MessageTransformer;

@Configuration
@EnableIntegration
public class IntegrationConfig {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(IntegrationConfig.class);
    private final TcpServerProperties properties;

    private final MessageTransformer messageTransformer;

    public IntegrationConfig(TcpServerProperties props, MessageTransformer messageTransformer) {
        this.properties = props;
        this.messageTransformer = messageTransformer;
    }

    @Bean
    public DirectChannel sendToRabbitQueue() {
        return new DirectChannel();
    }

   // @Bean
    public IntegrationFlow integrationFlow() {

        return IntegrationFlows.from(Tcp.inboundGateway(Tcp.nioServer(10021)
                .deserializer(TcpCodecs.crlf())
                .get()))
                .transform(Transformers.objectToString())
                .transform(this.messageTransformer)
                .get();
    }


}
