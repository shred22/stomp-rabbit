package com.boot.tcp.config;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.tcp.serializer.TcpCodecs;

import com.boot.tcp.config.properties.TcpServerProperties;
import com.boot.tcp.transformer.MessageTransformer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Configuration
//@EnableIntegration
public class IntegrationConfig {

    private final TcpServerProperties properties;

    private final MessageTransformer messageTransformer;

    public IntegrationConfig(TcpServerProperties props, MessageTransformer messageTransformer) {
        this.properties = props;
        this.messageTransformer = messageTransformer;
    }

    //@Bean
    public DirectChannel sendToRabbitQueue() {
        return new DirectChannel();
    }

    //@Bean
    public IntegrationFlow integrationFlow() {

        return IntegrationFlows.from(Tcp.inboundGateway(Tcp.nioServer(properties.getPort())
                .deserializer(TcpCodecs.crlf())
                .get()))
                .transform(Transformers.objectToString())
                .transform(this.messageTransformer)
                .channel("sendToRabbitQueue")
                .get();
    }

    @ServiceActivator(inputChannel = "sendToRabbitQueue", outputChannel = "")
    public String activator() {
        log.info("Received at Activator");
        return "custom";
    }
}
