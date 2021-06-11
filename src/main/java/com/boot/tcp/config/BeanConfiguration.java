package com.boot.tcp.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.javatuples.Pair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.boot.tcp.converter.DateDeserializer;
import com.boot.tcp.converter.DateSerializer;
import com.boot.tcp.model.DeviceConnectionDetails;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class BeanConfiguration {

    @Bean
    public ObjectMapper jsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(
                new SimpleModule("foo")
                        .addDeserializer(Date.class, new DateDeserializer())
                        .addSerializer(Date.class, new DateSerializer())
        );
        return mapper;
    }

    @Bean
    public Map<String, String> devicePayPointPair() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, Pair<Map<String, DeviceConnectionDetails>, Map<String, StompConnectionDetails>>> devicePayPointConnectionStatus() {
        return new HashMap<>();
    }

}
