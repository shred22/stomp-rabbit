package com.boot.stomp.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class StompRabbitApplication {

	public static void main(String[] args) {
		SpringApplication.run(StompRabbitApplication.class, args);
	}

}
