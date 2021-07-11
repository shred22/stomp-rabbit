package com.boot.stomp.tcp.model;

public class DeviceConnectionRequest {
    private String deviceId;
    private String deviceType;
    private String deviceStatus;

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public String getDeviceStatus() {
        return this.deviceStatus;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    @Override
    public String toString() {
        return "DeviceConnectionRequest: {" +
                "deviceId='" + deviceId + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", deviceStatus='" + deviceStatus + '\'' +
                '}';
    }
}
