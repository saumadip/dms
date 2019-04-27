package com.fs.dms.device;

/**
 * Device invalidation strategy to invalidate a device which has past its time and failed to respond
 *
 * @author saumadip mazumder
 **/
public interface IDeviceInvalidationStrategy
{
    /**
     * Called when a device is updated with a STATUS
     * this invalidate the previous timestamp or Timer of a device it is associated with
     * @param iDevice
     * @param status
     */
    void invalidate(IDevice iDevice,IDevice.Status status);
}
