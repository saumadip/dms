package com.fs.dms.device;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *  Extending {@code AbstractDevice} class equips a device with
 *
 * UUID deviceID;
 * String deviceName;
 * String secretKey;
 * Status deviceStatus;
 *
 * @author Saumadip Mazumder
 */
public abstract class AbstractDevice implements IDevice
{

    //Unique DeviceID
    protected final UUID deviceID;

    //Device Name
    protected final String deviceName;

    //SecretKey Associated with device
    @JsonIgnore
    protected final String secretKey;

    //Device status i.e. NEW,OK,UNHEALTHY,STALE
    protected volatile Status deviceStatus;

    //Invalidator service that marks device as STALE based on service business logic
    @JsonIgnore
    protected final IDeviceInvalidationStrategy invalidationStrategy;


   // @JsonIgnore
    //private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    protected AbstractDevice(UUID deviceID, String deviceName, String secretKey, IDeviceInvalidationStrategy invalidationStrategy)
    {
        this.deviceID = deviceID;
        this.deviceName = deviceName;
        this.secretKey = secretKey;
        this.invalidationStrategy = invalidationStrategy;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public String getSecretKey()
    {
        return secretKey;
    }

    public Status getDeviceStatus()
    {

        return deviceStatus;

    }

    public  void  updateDeviceStatus(Status deviceStatus)
    {
        this.deviceStatus = deviceStatus;

    }

    public UUID getDeviceID()
    {
        return deviceID;
    }

    public IDeviceInvalidationStrategy getInvalidationStrategy()
    {
        return invalidationStrategy;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof AbstractDevice)) return false;

        AbstractDevice that = (AbstractDevice) o;

        if (!getDeviceID().equals(that.getDeviceID())) return false;
        if (!getDeviceName().equals(that.getDeviceName())) return false;

        return getSecretKey().equals(that.getSecretKey());
    }

    @Override
    public int hashCode()
    {
        int result = getDeviceID().hashCode();

        result = 31 * result + getDeviceName().hashCode();
        result = 31 * result + getSecretKey().hashCode();
        return result;
    }
}
