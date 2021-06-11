package com.boot.tcp.model;

import lombok.Data;

@Data
public class DeviceConnectionRequest {
    private String deviceId;
    private String deviceType;
    private String deviceStatus;
}
