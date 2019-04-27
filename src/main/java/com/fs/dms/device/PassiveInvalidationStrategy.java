package com.fs.dms.device;

import java.time.Instant;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Invalidation strategy enabled during server startup
 *
 *  PASSIVE: When invalidate is called with {@Code Status.OK}
 *   it updates the timeStamp for the {@code PassiveIvalidationService}
 *   to invalidate based on invalidation logic
 *
 * @author saumadip mazumder
 */
public class PassiveInvalidationStrategy implements IDeviceInvalidationStrategy {


    private volatile Instant time;

    //private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    public PassiveInvalidationStrategy()
    {
        this.time = Instant.now();

    }

    /**
     * Sets the timer for the device to monitor the device status change
     * @param iDevice
     * @param status
     */
    @Override
    public void invalidate(IDevice iDevice,IDevice.Status status)
    {

        if(status.equals(IDevice.Status.OK))
        {
                this.time = Instant.now();
        }

    }

    public Instant getTime()
    {
            return time;
    }
}
