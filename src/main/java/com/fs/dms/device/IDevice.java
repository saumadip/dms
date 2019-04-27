package com.fs.dms.device;

import java.util.UUID;

/**
 * {@IDevice} interface should be implemented by all device in this system
 * The intent is to provide the following capabilities to all the devices.
 *
 * @author saumadip mazumder
 */
public interface IDevice
{

    /**
     * Return the name of the device which should be String
     * @return String
     */
    String getDeviceName();

    /**
     * Returns the secret key associated with any device
     * @return
     */
    String getSecretKey();

    /**
     * Returns device status
     * @return
     */
    Status getDeviceStatus();

    /**
     * Implement method to update device status
     * @param deviceStatus
     */
    void  updateDeviceStatus(Status deviceStatus);

    /**
     * Implement method to get the deviceID
     * @return
     */
    UUID getDeviceID();


    /**
     * Every device is injected with an invalidation strategy.
     *
     * @return invalidationStrategy
     */
    IDeviceInvalidationStrategy getInvalidationStrategy();

    /**
     * {@Staus} represents the available status that can be set for a device
     *
     * NEW: Set when a device is created
     * OK: Can be set by the User when device is in healthy condition
     * UNHEALTHY: Can be set by the User when device is in unhealthy condition
     * STALE: This is set automatically by the Timer, after a fixed delay if Status is OK
     *         and device is not updated within the delay
     */
    enum Status
    {
        NEW,OK,UNHEALTHY,STALE
    }

}
