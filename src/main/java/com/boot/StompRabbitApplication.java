package com.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.integration.config.EnableIntegration;

@EnableIntegration
@SpringBootApplication
@EnableConfigurationProperties
@EnableMongoRepositories(basePackages = "com.boot.stomp.rabbit")
public class StompRabbitApplication {
	public static void main(String[] args) {
		SpringApplication.run(StompRabbitApplication.class, args);
	}
}
