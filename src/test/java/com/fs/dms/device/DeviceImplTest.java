package com.fs.dms.device;

import com.fs.dms.exceptions.InvalidDeviceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DeviceImplTest
{

    @Mock
    IDeviceInvalidationStrategy IDeviceInvalidationStrategy;

    private IDevice device;

    @Test(expected = InvalidDeviceException.class)
    public void createDevice_SpecialChars()
    {
        device = new DeviceImpl("@SPtest$","mysecret", IDeviceInvalidationStrategy);
    }

    @Test
    public void createDevice_space_deviceName()
    {
        device = new DeviceImpl("Space device","mysecret", IDeviceInvalidationStrategy);

        assertEquals("Space device",device.getDeviceName());
    }

    @Test(expected = NullPointerException.class)
    public void createDevice_null()
    {
        device = new DeviceImpl(null,"mysecret", IDeviceInvalidationStrategy);
    }

    @Test(expected = NullPointerException.class)
    public void createDevice_empty()
    {
        device = new DeviceImpl("","mysecret", IDeviceInvalidationStrategy);
    }

    @Test(expected = NullPointerException.class)
    public void createDevice_emptySecret()
    {
        device = new DeviceImpl("","mysecret", IDeviceInvalidationStrategy);
    }

    @Test(expected = NullPointerException.class)
    public void createDevice_nullSecret()
    {
        device = new DeviceImpl("Mydevice",null, IDeviceInvalidationStrategy);
    }

    @Test
    public void createDevice_happyflowdevice()
    {
        device = new DeviceImpl("Mydevice","Mysecret", IDeviceInvalidationStrategy);

        assertEquals("Mydevice",device.getDeviceName());
        assertEquals("Mysecret",device.getSecretKey());
    }
}