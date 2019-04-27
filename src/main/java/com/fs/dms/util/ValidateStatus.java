package com.fs.dms.util;

import com.fs.dms.device.IDevice;

public class ValidateStatus
{
    public static boolean validateStatus(String status)
    {
        for (IDevice.Status statcheck : IDevice.Status.values())
        {
            if (statcheck.name().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
