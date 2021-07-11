package com.boot.stomp.rabbit.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.javatuples.Pair;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.boot.stomp.rabbit.model.DevicePayPointPair;
import com.boot.stomp.rabbit.repository.DevicePayPointPairRepository;
import com.boot.stomp.rabbit.service.PayPointService;
import com.boot.stomp.rabbit.model.StompConnectionDetails;
import com.boot.stomp.tcp.model.DeviceConnectionDetails;

import static com.boot.Constants.PAY_POINT_ID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final PayPointService payPointService;
    private final Map<String, Pair<Map<String, DeviceConnectionDetails>, Map<String, StompConnectionDetails>>> devicePayPointConnectionStatus;
    private final DevicePayPointPairRepository devicePayPointPairRepository;

    public WebSocketHandshakeInterceptor(PayPointService payPointService,
                                         Map<String, Pair<Map<String, DeviceConnectionDetails>,
                                              Map<String, StompConnectionDetails>>> devicePayPointConnectionStatus,
                                         DevicePayPointPairRepository devicePayPointPairRepository) {
        this.payPointService = payPointService;
        this.devicePayPointConnectionStatus = devicePayPointConnectionStatus;
        this.devicePayPointPairRepository = devicePayPointPairRepository;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> attributes) {
        log.info("Intercepting Handshake");
        String payPointId = serverHttpRequest.getURI().getPath().substring(10);
        DevicePayPointPair devicePayPointPair = devicePayPointPairRepository.findByPayPointId(payPointId);
        if (this.payPointService.getRegisteredPayPoints().contains(payPointId) && devicePayPointPair != null) {
            attributes.put(PAY_POINT_ID, payPointId);
            Pair<Map<String, DeviceConnectionDetails>, Map<String, StompConnectionDetails>> devicePayPointPairMap = devicePayPointConnectionStatus.get(devicePayPointPair.getDeviceId());
            if (devicePayPointPairMap == null) {
                StompConnectionDetails stompConnectionDetails = new StompConnectionDetails();
                stompConnectionDetails.setConnectionStatus("ACTIVE");
                stompConnectionDetails.setPayPointId(payPointId);
                stompConnectionDetails.setPayPointName(payPointId);
                Map<String, StompConnectionDetails> stompConnectionDetailsMap = new HashMap<>();
                stompConnectionDetailsMap.put(devicePayPointPair.getPayPointId(), stompConnectionDetails);
                DeviceConnectionDetails deviceConnectionDetails = new DeviceConnectionDetails(devicePayPointPair.getDeviceId(), null, "INACTIVE", false);
                Map<String, DeviceConnectionDetails> deviceConnectionDetailsMap = new HashMap<>();
                deviceConnectionDetailsMap.put(devicePayPointPair.getDeviceId(), deviceConnectionDetails);
                devicePayPointConnectionStatus.put(devicePayPointPair.getDeviceId(), Pair.with(deviceConnectionDetailsMap, stompConnectionDetailsMap));
            } else {
                Map<String, DeviceConnectionDetails> deviceConnectionDetailsMap = devicePayPointPairMap.getValue0();
                StompConnectionDetails stompConnectionDetails = new StompConnectionDetails();
                stompConnectionDetails.setConnectionStatus("ACTIVE");
                stompConnectionDetails.setPayPointId(payPointId);
                stompConnectionDetails.setPayPointName(payPointId);
                Map<String, StompConnectionDetails> stompConnectionDetailsMap = new HashMap<>();
                stompConnectionDetailsMap.put(devicePayPointPair.getPayPointId(), stompConnectionDetails);
                devicePayPointConnectionStatus.put(devicePayPointPair.getDeviceId(), Pair.with(deviceConnectionDetailsMap, stompConnectionDetailsMap));
            }
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest,
                               ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception ex) {
    }
}