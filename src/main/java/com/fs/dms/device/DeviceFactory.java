package com.fs.dms.device;

import java.util.Timer;

/**
 * Creates {@Code IDevice} with specific Invalidation strategy enabled during server startup
 *
 * The Strategy are ACTIVE,PASSIVE
 *
 *  ACTIVE: See {@Code ActiveInvalidationStrategy}
 *  PASSIVE: See {@Code PassiveInvalidationStrategy}
 *
 * @author saumadip mazumder
 */
public class DeviceFactory
{

    private int delayinSec;

    private Timer timer;

    private Strategy strategy;

    /**
     * Creates a device factory
     * @param delayinSec
     * @param timer
     * @param strategy
     */
    public DeviceFactory(int delayinSec, Timer timer, Strategy strategy)
    {
        this.delayinSec = delayinSec;
        this.timer = timer;
        this.strategy = strategy;
    }

    /**
     * Creates and returns a device based on the {@code Strategy}
     * @param deviceName
     * @param secretKey
     * @return
     */
    public IDevice createDevice(String deviceName, String secretKey)
    {

        if(strategy.equals(Strategy.PASSIVE))
        {
            return new DeviceImpl(deviceName,secretKey,new PassiveInvalidationStrategy());
        }
        return new DeviceImpl(deviceName,secretKey,new ActiveInvalidationStrategy(timer,delayinSec));

    }

    /**
     * Enum Strategy ACTIVE and PASSIVE
     */
    public enum Strategy
    {
        ACTIVE,PASSIVE
    }

}
