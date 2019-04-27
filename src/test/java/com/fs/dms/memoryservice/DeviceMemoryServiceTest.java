package com.fs.dms.memoryservice;

import com.fs.dms.device.DeviceImpl;
import com.fs.dms.device.IDeviceInvalidationStrategy;
import com.fs.dms.device.IDevice;
import com.fs.dms.rest.pojo.Device;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DeviceMemoryServiceTest {

    @Mock
    IDeviceInvalidationStrategy IDeviceInvalidationStrategy;


    DeviceMemoryService deviceMemoryService;

    Map<UUID,IDevice> deviceMAp =  new ConcurrentHashMap<>();

    @Before
    public void setup()
    {
        deviceMemoryService = new DeviceMemoryService( deviceMAp);
    }

    @Test
    public void save()
    {

        IDevice iDevice = new DeviceImpl("test","test", IDeviceInvalidationStrategy);

        deviceMemoryService.save(iDevice);

        assertEquals(1,deviceMAp.size());
    }

    @Test
    public void get()
    {
        IDevice iDevice = new DeviceImpl("test","test", IDeviceInvalidationStrategy);

        UUID deviceID = iDevice.getDeviceID();

        deviceMemoryService.save(iDevice);

        IDevice iDeviceReturned = deviceMemoryService.get(deviceID);

        assertEquals(iDevice,iDeviceReturned);
    }

    @Test
    public void update_OK()
    {
        IDevice iDevice = new DeviceImpl("testUpdate","testSecret", IDeviceInvalidationStrategy);

        Mockito.doNothing().when(IDeviceInvalidationStrategy).invalidate(iDevice,IDevice.Status.OK);

        UUID deviceID = iDevice.getDeviceID();

        deviceMemoryService.save(iDevice);

        Device device = new Device(iDevice);
        device.setDeviceStatus(IDevice.Status.OK);

        deviceMemoryService.update(deviceID, device);

        assertEquals( IDevice.Status.OK,iDevice.getDeviceStatus());
    }


    @Test
    public void update_UNHEALTHY()
    {
        IDevice iDevice = new DeviceImpl("testUpdate","testSecret", IDeviceInvalidationStrategy);

        UUID deviceID = iDevice.getDeviceID();

        Mockito.doNothing().when(IDeviceInvalidationStrategy).invalidate(iDevice,IDevice.Status.UNHEALTHY);

        deviceMemoryService.save(iDevice);

        Device device = new Device(iDevice);
        device.setDeviceStatus(IDevice.Status.UNHEALTHY);

        deviceMemoryService.update(deviceID,device);

        assertEquals( IDevice.Status.UNHEALTHY,iDevice.getDeviceStatus());
    }
}