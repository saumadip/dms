package com.fs.dms.device;

import com.fs.dms.exceptions.InvalidDeviceException;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;


/**
 * Concrete implementation of {@IDevice}
 *
 * @author saumadip mazumder
 */
public class DeviceImpl extends AbstractDevice
{

    public DeviceImpl(String deviceName, String secretKey, IDeviceInvalidationStrategy invalidationStrategy)
    {

        super(UUID.randomUUID(), validateDevice(deviceName), validateSecretkey(secretKey), invalidationStrategy);

        this.deviceStatus = Status.NEW;

    }

    /**
     * Validates the device name as this type of Device should have human readable name
     * @param deviceName
     * @return
     */
    private static String validateDevice(String deviceName)
    {
        if(StringUtils.isEmpty(deviceName))
            throw new NullPointerException("Device can't be null or empty");

        if(!StringUtils.isAlphanumeric(deviceName) && !StringUtils.isAlphanumericSpace(deviceName))
            throw new InvalidDeviceException("Device with special characters not allowed");

        return deviceName;
    }

    /**
     * Validates secretKey based on current usecase
     * @param secretKey
     * @return
     */
    private static String validateSecretkey(String secretKey)
    {
        if(StringUtils.isEmpty(secretKey))
            throw new NullPointerException("Device secret key can't be null or empty");
        return secretKey;
    }

}
