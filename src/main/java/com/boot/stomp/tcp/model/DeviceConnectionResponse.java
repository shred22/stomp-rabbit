package com.boot.stomp.tcp.model;

import java.util.Objects;

import lombok.Builder;

@Builder
public class DeviceConnectionResponse {
    private String deviceId;
    private String deviceConnectionStatus;

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceConnectionStatus() {
        return deviceConnectionStatus;
    }

    public void setDeviceConnectionStatus(String deviceConnectionStatus) {
        this.deviceConnectionStatus = deviceConnectionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceConnectionResponse that = (DeviceConnectionResponse) o;
        return Objects.equals(deviceId, that.deviceId) && Objects.equals(deviceConnectionStatus, that.deviceConnectionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, deviceConnectionStatus);
    }
}
