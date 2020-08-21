package com.acpay.acapytrade.LeftNavigation.Notes;

public class NotesPlacesDetails {
    private String id;
    private String deviceName;
    private String deviceType;
    private String deviceModel;
    private String deviceDetails;

    private String deviceIp;
    private String deviceUsername;
    private String devicePasswoed;
    private String devicePort;
    private String deviceEmail;
    private String deviceEmailPass;

    public NotesPlacesDetails(String id,String deviceName, String deviceType, String deviceModel, String deviceDetails, String deviceIp, String deviceUsername, String devicePasswoed, String devicePort, String deviceEmail, String deviceEmailPass) {
        this.id = id;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.deviceModel = deviceModel;
        this.deviceDetails = deviceDetails;
        this.deviceIp = deviceIp;
        this.deviceUsername = deviceUsername;
        this.devicePasswoed = devicePasswoed;
        this.devicePort = devicePort;
        this.deviceEmail = deviceEmail;
        this.deviceEmailPass = deviceEmailPass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(String deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDeviceUsername() {
        return deviceUsername;
    }

    public void setDeviceUsername(String deviceUsername) {
        this.deviceUsername = deviceUsername;
    }

    public String getDevicePasswoed() {
        return devicePasswoed;
    }

    public void setDevicePasswoed(String devicePasswoed) {
        this.devicePasswoed = devicePasswoed;
    }

    public String getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(String devicePort) {
        this.devicePort = devicePort;
    }

    public String getDeviceEmail() {
        return deviceEmail;
    }

    public void setDeviceEmail(String deviceEmail) {
        this.deviceEmail = deviceEmail;
    }

    public String getDeviceEmailPass() {
        return deviceEmailPass;
    }

    public void setDeviceEmailPass(String deviceEmailPass) {
        this.deviceEmailPass = deviceEmailPass;
    }

}
