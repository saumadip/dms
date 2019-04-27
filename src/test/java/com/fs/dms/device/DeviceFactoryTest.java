package com.fs.dms.device;

import org.junit.Test;

import java.util.Timer;

import static org.junit.Assert.*;

public class DeviceFactoryTest {

    @Test
    public void createActiveIvalidationDevice()
    {
        DeviceFactory deviceFactory = new DeviceFactory(10,new Timer(), DeviceFactory.Strategy.ACTIVE);

        IDevice device = deviceFactory.createDevice("test", "test");

        assertTrue(device.getInvalidationStrategy() instanceof ActiveInvalidationStrategy);
    }

    @Test
    public void createPassiveIvalidationDevice()
    {
        DeviceFactory deviceFactory = new DeviceFactory(10,new Timer(), DeviceFactory.Strategy.PASSIVE);

        IDevice device = deviceFactory.createDevice("test", "test");

        assertTrue(device.getInvalidationStrategy() instanceof PassiveInvalidationStrategy);

    }
}