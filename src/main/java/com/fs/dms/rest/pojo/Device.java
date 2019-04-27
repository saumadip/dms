package com.fs.dms.rest.pojo;

import com.fs.dms.device.IDevice;

import java.io.Serializable;


public class Device implements Serializable {


    private String deviceName;


    private String secretKey;

    private IDevice.Status deviceStatus;

    public Device() {
    }

    public Device(String deviceName, String secretKey, IDevice.Status deviceStatus) {
        this.deviceName = deviceName;
        this.secretKey = secretKey;
        this.deviceStatus = deviceStatus;
    }

    public Device(IDevice iDevice) {
        this.deviceName = iDevice.getDeviceName();
        this.secretKey = iDevice.getSecretKey();
        this.deviceStatus = iDevice.getDeviceStatus();

    }

    public String getDeviceName() {
        return deviceName;
    }


    public String getSecretKey() {
        return secretKey;
    }


    public IDevice.Status getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(IDevice.Status deviceStatus) {
        this.deviceStatus = deviceStatus;
    }
}
