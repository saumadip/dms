package com.fs.dms.device;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.Instant;

import static org.junit.Assert.*;


public class PassiveInvalidationStrategyTest {

    PassiveInvalidationStrategy passiveInvalidationStrategy;

    @Mock
    IDevice iDevice;

    @Before
    public void setup()
    {
        passiveInvalidationStrategy = new PassiveInvalidationStrategy();
    }

    @Test
    public void invalidate_OK() throws InterruptedException {
        Instant creationTime = passiveInvalidationStrategy.getTime();

        Thread.sleep(100);
        passiveInvalidationStrategy.invalidate(iDevice, IDevice.Status.OK);

        assertTrue(passiveInvalidationStrategy.getTime().compareTo( creationTime) > 0);

    }


    @Test
    public void invalidate_UNHEALTHY() throws InterruptedException {
        Instant creationTime = passiveInvalidationStrategy.getTime();

        Thread.sleep(100);
        passiveInvalidationStrategy.invalidate(iDevice, IDevice.Status.UNHEALTHY);

        assertEquals(0, passiveInvalidationStrategy.getTime().compareTo(creationTime));

    }

}