package com.boot.stomp.tcp.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.boot.stomp.rabbit.config.properties.BaseConfigProperties;

@Configuration
@ConfigurationProperties("app.tcp.server")
public class TcpServerProperties extends BaseConfigProperties {
}
