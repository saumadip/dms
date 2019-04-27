package com.fs.dms.config;

import com.fs.dms.device.DeviceFactory;
import com.fs.dms.device.IDevice;
import com.fs.dms.services.PassiveInvalidationService;
import com.fs.dms.memoryservice.DeviceMemoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.*;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "device")
public class AppConfig
{
    @Value("${device.invalidationTimeinSec}")
    private int invalidationTimeinSec;

    @Value("${device.passiveStrategyCycleInSec}")
    private long passiveStrategyCycleInSec;

    private Map<UUID, IDevice> deviceMap = new ConcurrentHashMap<>();

    @Value("${device.invalidationStrategy}")
    private DeviceFactory.Strategy invalidationStrategy;

    @Bean
    public DeviceMemoryService getDeviceMemoryService()
    {
        return new DeviceMemoryService(deviceMap);
    }

    @Bean
    public DeviceFactory getDeviceFactory()
    {
        return new DeviceFactory(invalidationTimeinSec,new Timer(true), invalidationStrategy);
    }

    @Bean
    public void setInvalidationStrategy()
    {
        if(DeviceFactory.Strategy.PASSIVE.equals(invalidationStrategy))
        {
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleWithFixedDelay(new PassiveInvalidationService(deviceMap, invalidationTimeinSec),1L, passiveStrategyCycleInSec,TimeUnit.SECONDS);
        }
    }
}
