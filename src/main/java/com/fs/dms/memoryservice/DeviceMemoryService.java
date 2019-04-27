package com.fs.dms.memoryservice;

import com.fs.dms.device.IDevice;
import com.fs.dms.exceptions.DeviceNotFoundException;
import com.fs.dms.exceptions.UnauthorizedException;
import com.fs.dms.rest.pojo.Device;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * DeviceMemoryService is the in memory DeviceStorage
 *
 * @author saumadip mazumder
 */
public class DeviceMemoryService
{

    //In memory device cache
    private Map<UUID,IDevice> deviceMAp;


    public DeviceMemoryService(Map<UUID, IDevice> deviceMAp)
    {
        this.deviceMAp = deviceMAp;
    }


    /**
     *  Saves device to deviceMap
     * @param newIDevice
     * @return
     */
    public IDevice save(IDevice newIDevice)
    {
        deviceMAp.put(newIDevice.getDeviceID(), newIDevice);

        return newIDevice;
    }

    /**
     * Gets device from deviceMap by device ID
     * @param deviceId
     * @return
     */
    public IDevice get(UUID deviceId)
    {
        IDevice iDevice = deviceMAp.get(deviceId);

        if(iDevice == null)
            throw new DeviceNotFoundException("Device with id "+deviceId+" Notfound");

        return iDevice;
    }

    /**
     * Used to update device status
     * @param uuid
     * @param device
     * @return
     */
    public IDevice update(UUID uuid, Device device)
    {

        IDevice iDevice = get(uuid);

        if(!iDevice.getSecretKey().equals(device.getSecretKey()))
            throw new UnauthorizedException("Not authorized to update device status");

        if(!iDevice.getDeviceName().equals(device.getDeviceName()))
            throw new UnauthorizedException("Device name and id mismatch");

        iDevice.updateDeviceStatus(device.getDeviceStatus());
        iDevice.getInvalidationStrategy().invalidate(iDevice,device.getDeviceStatus());
        return iDevice;

    }

    /**
     * Returns the in memory cache
     * @return
     */
    public Map<UUID, IDevice> getDeviceMAp()
    {
        return Collections.unmodifiableMap(deviceMAp);
    }
}
