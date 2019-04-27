package com.fs.dms.device;

import java.util.Timer;
import java.util.TimerTask;

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
public class ActiveInvalidationStrategy implements IDeviceInvalidationStrategy {


    private final Timer timer;

    private final int invalidationTimeInSec;

    private TimerTask timerTask;

    public ActiveInvalidationStrategy(Timer timer, int invalidationTimeInSec)
    {
        this.timer = timer;
        this.invalidationTimeInSec = invalidationTimeInSec;

    }


    /**
     * DeviceInvalidator marks device as Stale after the specified invalidationTimeInSec Timer
     */
    class DeviceInvalidator extends TimerTask
    {

        private final IDevice iDevice;

        public DeviceInvalidator(IDevice iDevice)
        {
            this.iDevice = iDevice;
        }

        @Override
        public void run()
        {
            iDevice.updateDeviceStatus(IDevice.Status.STALE);
        }


    }


    /**
     * Sets the timer for the device to monitor the device status change
     * @param iDevice
     * @param status
     */
    @Override
    public void invalidate(IDevice iDevice,IDevice.Status status)
    {

        if(this.timerTask != null)
            timerTask.cancel();

        timer.purge();

        if(status.equals(IDevice.Status.OK))
        {
            this.timerTask = new DeviceInvalidator(iDevice);
            timer.schedule(timerTask, invalidationTimeInSec * 1000);
        }

    }



}
