package com.boot.stomp.rabbit.jwt;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.boot.stomp.rabbit.config.properties.JwtConfigProperties;


@Configuration
public class JwtSecretKey {

    private final JwtConfigProperties jwtConfigProperties;

    @Autowired
    public JwtSecretKey(JwtConfigProperties jwtConfigProperties) {
        this.jwtConfigProperties = jwtConfigProperties;
    }

    @Bean
    public SecretKey secretKey() {
        return new SecretKeySpec(jwtConfigProperties.getSecretKey().getBytes(), "HMACSHA256");
    }
}
