package com.boot.stomp.rabbit.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseConfigProperties {
    private String endpoint;
    private String userDestinationPrefix;
    private String appDestinationPrefix;
    private String allowedOrigins;
    private String host;
    private int port;
    private String login;
    private String password;
    private String relay;
    private String certFile;
    private String privateKeyFile;
    private int threadPoolSize;
    private String destinationToken;
    private String destinationType;
    private String version;
    private String keyPassword;
    private String KeyStoreType;
    private String KeyLocation;
    private String trustPassword;
    private String trustStoreType;
    private String trustManagerType;
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;
    private String issuer;
    private SSL ssl;
}

