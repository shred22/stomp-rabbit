package com.boot.stomp.rabbit.config;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StompSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("\n\n\n\n\n\n");

        log.info("************ Setting auto ACK ************");
        log.info("\n\n\n\n\n\n");
        session.setAutoReceipt(true);
    }
}
