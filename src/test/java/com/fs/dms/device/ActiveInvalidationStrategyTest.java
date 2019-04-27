package com.fs.dms.device;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Timer;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ActiveInvalidationStrategyTest {

    private ActiveInvalidationStrategy activeInvalidationStrategy;

    private IDevice iDevice;


    @Before
    public void setup()
    {
        activeInvalidationStrategy = new ActiveInvalidationStrategy(new Timer(),1);
        iDevice = new DeviceImpl("Test","Test",activeInvalidationStrategy);
    }

    @Test
    public void invalidate_withOK() throws InterruptedException
    {
        activeInvalidationStrategy.invalidate(iDevice, IDevice.Status.OK);

        Thread.sleep(1500);

        assertEquals(IDevice.Status.STALE,iDevice.getDeviceStatus());
    }


    @Test
    public void invalidate_withOK_AssertbeforeTime()
    {
        activeInvalidationStrategy.invalidate(iDevice, IDevice.Status.OK);

        assertEquals(IDevice.Status.NEW,iDevice.getDeviceStatus());
    }

    @Test
    public void invalidate_twice_withMultipleStatus() throws InterruptedException
    {
        activeInvalidationStrategy.invalidate(iDevice, IDevice.Status.OK);
        Thread.sleep(100);
        //just cancelling the existing timerTask, but not really updating the device status
        activeInvalidationStrategy.invalidate(iDevice, IDevice.Status.UNHEALTHY);
        Thread.sleep(1500);

        assertEquals(IDevice.Status.NEW,iDevice.getDeviceStatus());
    }


}