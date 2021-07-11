package com.boot.stomp.rabbit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection= "DevicePayPointPair")
public class DevicePayPointPair {

    @Id
    private String id;
    @Indexed(name = "deviceIdIndex")
    private String deviceId;
    @Indexed(name = "payPointIdIndex")
    private String payPointId;

    public DevicePayPointPair() {
    }

    public DevicePayPointPair(String deviceId, String payPointId) {
        this.deviceId = deviceId;
        this.payPointId = payPointId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPayPointId() {
        return payPointId;
    }

    public void setPayPointId(String payPointId) {
        this.payPointId = payPointId;
    }
}
