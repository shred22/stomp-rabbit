package com.boot.stomp.rabbit.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.boot.stomp.rabbit.service.PayPointService;

import static com.boot.Constants.POS_ID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    private final PayPointService payPointService;

    public CustomHandshakeInterceptor(PayPointService payPointService) {
        this.payPointService = payPointService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
            WebSocketHandler webSocketHandler, Map<String, Object> attributes) {
        log.info("Intercepting Handshake");
        String posIdInPath = serverHttpRequest.getURI().getPath().substring(10);
        if(this.payPointService.getRegisteredPayPoints().contains(posIdInPath)) {
            attributes.put(POS_ID, posIdInPath);
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest,
                               ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception ex) {
    }
}