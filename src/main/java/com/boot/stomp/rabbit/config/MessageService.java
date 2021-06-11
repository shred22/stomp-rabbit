package com.boot.stomp.rabbit.config;

public interface MessageService {
    byte[] processMessage(byte[] message);
}
