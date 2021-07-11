package com.boot.stomp.rabbit.model;

public class StompConnectionDetails {
    private String payPointId;
    private String payPointName;
    private String connectionStatus;

    public String getPayPointId() {
        return payPointId;
    }

    public void setPayPointId(String payPointId) {
        this.payPointId = payPointId;
    }

    public String getPayPointName() {
        return payPointName;
    }

    public void setPayPointName(String payPointName) {
        this.payPointName = payPointName;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

}
