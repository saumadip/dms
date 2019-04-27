package com.fs.dms.device;

import com.fs.dms.services.PassiveInvalidationService;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class PassiveInvalidationServiceTest {


    private PassiveInvalidationService passiveInvalidationService;

    private Map<UUID,IDevice> deviceMAp = new ConcurrentHashMap<>();

    private ScheduledExecutorService serviceThread;


    @Before
    public void setup()
    {
        passiveInvalidationService =  new PassiveInvalidationService(deviceMAp,2);
        serviceThread= Executors.newSingleThreadScheduledExecutor();
        serviceThread.scheduleAtFixedRate(passiveInvalidationService,0L,1L, TimeUnit.SECONDS);

    }


    @Test
    public void testIf_All_OK_device_MarkedStale() throws InterruptedException
    {

        int i=0;
        while( i <10)
        {
            IDevice iDevice = new DeviceImpl("test", "test", new PassiveInvalidationStrategy());
            iDevice.updateDeviceStatus(IDevice.Status.OK);
            deviceMAp.put(iDevice.getDeviceID(), iDevice);
            i++;
        }

        Thread.sleep(7000);

        for(IDevice iDevice :deviceMAp.values())
        {
            assertEquals(IDevice.Status.STALE,iDevice.getDeviceStatus());
        }

    }


    @Test
    public void testIf_TWO_UNHEALTHY_device_MarkedStale() throws InterruptedException
    {

        int i=0;
        while( i <20)
        {
            IDevice iDevice = new DeviceImpl("test", "test", new PassiveInvalidationStrategy());
            if(i == 15 || i== 5)
            {
                iDevice.updateDeviceStatus(IDevice.Status.UNHEALTHY);
            }
            else {
                iDevice.updateDeviceStatus(IDevice.Status.OK);
            }
            deviceMAp.put(iDevice.getDeviceID(), iDevice);
            i++;
        }

        Thread.sleep(7000);
        int unhealthyDeviceCount=0;
        for(IDevice iDevice :deviceMAp.values())
        {

            if(iDevice.getDeviceStatus().equals(IDevice.Status.UNHEALTHY)) {
                unhealthyDeviceCount++;
                continue;
            }

            assertEquals(IDevice.Status.STALE, iDevice.getDeviceStatus());
        }
        assertEquals(2,unhealthyDeviceCount);
    }

}