package com.fs.dms.services;

import com.fs.dms.device.IDevice;
import com.fs.dms.device.PassiveInvalidationStrategy;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * When PASSIVE Invalidation strategy enabled during server startup, this
 * PassiveInvalidationService is started which invalidated device whose STATUS are OK
 * and are falling behind given invalidationTimeInSec time
 *
 * @author saumadip mazumder
 */
public class PassiveInvalidationService implements Runnable
{

    final static Logger logger = Logger.getLogger(PassiveInvalidationService.class.getName());

    /**
     *  Collection containing all the devices
     */
    private Map<UUID, IDevice> deviceMap;

    /**
     * STALE time spesicfied in SEC
     */
    private int invalidationTimeInSec;


    public PassiveInvalidationService(Map<UUID, IDevice> deviceMap, int invalidationTimeInSec)
    {
        this.deviceMap = deviceMap;
        this.invalidationTimeInSec = invalidationTimeInSec;
    }

    /**
     * Asynchronously does the following
     *
     * 1. Filters devices with OK status
     * 2. For every device checks the time if it has past the staleTime invalidationTimeInSec
     *
     */
    @Override
    public void run()
    {

            if(!deviceMap.values().isEmpty()) {

                deviceMap.values()
                        .stream()
                        .filter(device -> device.getDeviceStatus().equals(IDevice.Status.OK))
                        .forEach(iDevice -> {

                            if (!(iDevice.getInvalidationStrategy() instanceof PassiveInvalidationStrategy))
                                throw new IllegalStateException("PassiveInvalidationService can only work with PassiveInvalidationStrategy");

                            PassiveInvalidationStrategy passiveInvalidationStrategy = (PassiveInvalidationStrategy) iDevice.getInvalidationStrategy();

                            Instant timenow = Instant.now();

                            if (Duration.between(passiveInvalidationStrategy.getTime(),timenow).getSeconds() >= invalidationTimeInSec)
                            {
                                if(iDevice.getDeviceStatus().equals(IDevice.Status.OK)) {
                                    iDevice.updateDeviceStatus(IDevice.Status.STALE);
                                }
                            }

                        });
            }

    }

}
