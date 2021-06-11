package com.boot.tcp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceConnectionDetails {
    private String deviceId;
    private String deviceType;
    private String connectionStatus;
}
