package com.boot.tcp;

import java.util.HashMap;
import java.util.Map;

import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import com.boot.tcp.config.StompConnectionDetails;
import com.boot.tcp.model.DeviceConnectionDetails;
import com.boot.tcp.model.DeviceConnectionRequest;

import static com.boot.tcp.model.DeviceConnectionDetails.builder;
import lombok.AllArgsConstructor;
import static org.javatuples.Pair.with;

@Service
@AllArgsConstructor
public class ConnectionDetailsService {

    private final Map<String, String> devicePayPointPair;
    private final Map<String, Pair<Map<String, DeviceConnectionDetails>, Map<String, StompConnectionDetails>>> devicePayPointConnectionStatus;

    public void updateConnectionDetails(DeviceConnectionRequest request) { ;
        devicePayPointConnectionStatus.put(request.getDeviceId(), with(getDeviceDetails(request), getStompDetails(request.getDeviceId())));
    }

    private Map<String, StompConnectionDetails> getStompDetails(String deviceId) {
        HashMap<String, StompConnectionDetails> stompDetailsMap = new HashMap<>();
        String payPointId = devicePayPointPair.get(deviceId);
        stompDetailsMap.put(payPointId, null);
        return stompDetailsMap;
    }

    private Map<String, DeviceConnectionDetails> getDeviceDetails(DeviceConnectionRequest request) {
        Map<String, DeviceConnectionDetails> deviceDetails = new HashMap<>();
        deviceDetails.put(request.getDeviceId(), builder().connectionStatus("ACTIVE")
                .deviceId(request.getDeviceId())
                .deviceType(request.getDeviceType())
                .build());
        return deviceDetails;
    }
}
