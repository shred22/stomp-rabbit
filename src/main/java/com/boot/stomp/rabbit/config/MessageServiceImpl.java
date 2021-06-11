package com.boot.stomp.rabbit.config;


import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Override
    public byte[] processMessage(byte[] message) {
        String messageContent = new String(message);
        log.info("Receive message: {}", messageContent);
        String responseContent = String.format("Message \"%s\" is processed", messageContent);
        return responseContent.getBytes();
    }

}