package com.boot;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.integration.config.EnableIntegration;

@EnableIntegration
@SpringBootApplication
@EnableConfigurationProperties
public class StompRabbitApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(StompRabbitApplication.class, args);
	}

}
