package com.boot.stomp.tcp.model;

public class DeviceConnectionDetails {
    private String deviceId;
    private String deviceType;
    private String connectionStatus;
    private Boolean isQueueCreated;

    public DeviceConnectionDetails(String deviceId, String deviceType, String connectionStatus, Boolean isQueueCreated) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.connectionStatus = connectionStatus;
        this.isQueueCreated = isQueueCreated;
    }

    public static DeviceConnectionDetailsBuilder builder() {
        return new DeviceConnectionDetailsBuilder();
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public String getConnectionStatus() {
        return this.connectionStatus;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public Boolean getQueueCreated() {
        return isQueueCreated;
    }

    public void setQueueCreated(Boolean queueCreated) {
        isQueueCreated = queueCreated;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DeviceConnectionDetails)) return false;
        final DeviceConnectionDetails other = (DeviceConnectionDetails) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$deviceId = this.getDeviceId();
        final Object other$deviceId = other.getDeviceId();
        if (this$deviceId == null ? other$deviceId != null : !this$deviceId.equals(other$deviceId)) return false;
        final Object this$deviceType = this.getDeviceType();
        final Object other$deviceType = other.getDeviceType();
        if (this$deviceType == null ? other$deviceType != null : !this$deviceType.equals(other$deviceType))
            return false;
        final Object this$connectionStatus = this.getConnectionStatus();
        final Object other$connectionStatus = other.getConnectionStatus();
        if (this$connectionStatus == null ? other$connectionStatus != null : !this$connectionStatus.equals(other$connectionStatus))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DeviceConnectionDetails;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $deviceId = this.getDeviceId();
        result = result * PRIME + ($deviceId == null ? 43 : $deviceId.hashCode());
        final Object $deviceType = this.getDeviceType();
        result = result * PRIME + ($deviceType == null ? 43 : $deviceType.hashCode());
        final Object $connectionStatus = this.getConnectionStatus();
        result = result * PRIME + ($connectionStatus == null ? 43 : $connectionStatus.hashCode());
        return result;
    }

    public String toString() {
        return "DeviceConnectionDetails(deviceId=" + this.getDeviceId() + ", deviceType=" + this.getDeviceType() + ", connectionStatus=" + this.getConnectionStatus() + ")";
    }

    public static class DeviceConnectionDetailsBuilder {
        private String deviceId;
        private String deviceType;
        private String connectionStatus;
        private Boolean isQueueCreated;

        DeviceConnectionDetailsBuilder() {
        }

        public DeviceConnectionDetailsBuilder deviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public DeviceConnectionDetailsBuilder deviceType(String deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public DeviceConnectionDetailsBuilder connectionStatus(String connectionStatus) {
            this.connectionStatus = connectionStatus;
            return this;
        }

        public DeviceConnectionDetailsBuilder isQueueCreated(Boolean isQueueCreated) {
            this.isQueueCreated = isQueueCreated;
            return this;
        }

        public DeviceConnectionDetails build() {
            return new DeviceConnectionDetails(deviceId, deviceType, connectionStatus, isQueueCreated);
        }

        public String toString() {
            return "DeviceConnectionDetails.DeviceConnectionDetailsBuilder(deviceId=" + this.deviceId + ", deviceType=" + this.deviceType + ", connectionStatus=" + this.connectionStatus + ")";
        }
    }
}
