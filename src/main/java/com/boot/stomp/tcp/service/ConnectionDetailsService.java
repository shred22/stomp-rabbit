package com.boot.stomp.tcp.service;

import java.util.HashMap;
import java.util.Map;

import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import com.boot.stomp.rabbit.model.DevicePayPointPair;
import com.boot.stomp.rabbit.model.StompConnectionDetails;
import com.boot.stomp.rabbit.repository.DevicePayPointPairRepository;
import com.boot.stomp.rabbit.service.RabbitAdminService;
import com.boot.stomp.tcp.model.DeviceConnectionDetails;
import com.boot.stomp.tcp.model.DeviceConnectionRequest;

import static com.boot.stomp.tcp.model.DeviceConnectionDetails.builder;
import static java.lang.Boolean.TRUE;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConnectionDetailsService {

    private final DevicePayPointPairRepository repository;
    private final Map<String, Pair<Map<String, DeviceConnectionDetails>, Map<String, StompConnectionDetails>>> devicePayPointConnectionStatus;
    private final RabbitAdminService rabbitAdminService;


    public ConnectionDetailsService(DevicePayPointPairRepository repository,
                                    Map<String, Pair<Map<String, DeviceConnectionDetails>, Map<String, StompConnectionDetails>>> devicePayPointConnectionStatus,
                                    RabbitAdminService rabbitAdminService) {
        this.repository = repository;
        this.devicePayPointConnectionStatus = devicePayPointConnectionStatus;
        this.rabbitAdminService = rabbitAdminService;
    }

    public void updateConnectionDetails(DeviceConnectionRequest request) {
        Boolean rabbitOk = TRUE;
        //Check if Queue is already Created
        if(rabbitInfraCreationPreConditionCheck(request)) {
            rabbitOk = rabbitAdminService.handleAmqpInfraCreationForRoutingToExchange("stomp-exchange",
                    "direct", "TCP-Q-"+request.getDeviceId(),
                    request.getDeviceId(),
                    (msg) -> log.info("TCP Message Received, Checking inter-instance communication : " + new String(msg.getBody())));
        }
        devicePayPointConnectionStatus.put(request.getDeviceId(),
                Pair.with(getDeviceDetails(request, rabbitOk), getStompDetails(request.getDeviceId())));
    }

    private boolean rabbitInfraCreationPreConditionCheck(DeviceConnectionRequest request) {
        return devicePayPointConnectionStatus.get(request.getDeviceId()) == null ||
                !devicePayPointConnectionStatus.get(request.getDeviceId()).getValue0().get(request.getDeviceId()).getQueueCreated();
    }

    private Map<String, StompConnectionDetails> getStompDetails(String deviceId) {
        HashMap<String, StompConnectionDetails> stompDetailsMap = new HashMap<>();
        DevicePayPointPair devicePayPointPair = repository.findByDeviceId(deviceId);
        stompDetailsMap.put(devicePayPointPair.getPayPointId(), null);
        return stompDetailsMap;
    }

    private Map<String, DeviceConnectionDetails> getDeviceDetails(DeviceConnectionRequest request, Boolean rabbitOk) {
        Map<String, DeviceConnectionDetails> deviceDetails = new HashMap<>();
        deviceDetails.put(request.getDeviceId(), builder().connectionStatus("ACTIVE")
                .deviceId(request.getDeviceId())
                .deviceType(request.getDeviceType())
                .isQueueCreated(rabbitOk)
                .build());
        return deviceDetails;
    }

}
