package com.fs.dms.rest;

import com.fs.dms.device.DeviceFactory;
import com.fs.dms.device.IDevice;
import com.fs.dms.exceptions.InvalidDeviceException;
import com.fs.dms.exceptions.InvalidStatusUpdateException;
import com.fs.dms.memoryservice.DeviceMemoryService;
import com.fs.dms.rest.pojo.Device;
import com.fs.dms.services.GraphQLService;
import com.fs.dms.util.ValidateStatus;
import graphql.ExecutionResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *  The {@DeviceService} class facilitates the following
 *
 *  1. create: Lets user create and register a device.
 *  2. details: Retrieves device details from the store
 *  3. updateStatus: Allows users to update status of a device to OK or UNHEALTHY
 *
 *  {@DeviceMemoryService} facilitates storage and retrieval  of data from the memory
 *  This is expected to be provided/autowired by the running container
 *
 * @author Saumadip Mazumder
 */
@RestController
public class RestDeviceController
{

    @Autowired
    DeviceMemoryService deviceMemoryService;

    @Autowired
    DeviceFactory deviceFactory;

    @Autowired
    private GraphQLService graphQLService;


    /**
     * Device registration, creates and save a device in memory.
     * @param device
     * @return IDevice
     * @throws InvalidDeviceException
     */
    @PostMapping("/device")
    public ResponseEntity<IDevice> create(@RequestBody Device device)
    {

        IDevice newIDevice = deviceFactory.createDevice(device.getDeviceName(),device.getSecretKey());

        IDevice savedDevice = deviceMemoryService.save(newIDevice);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{deviceId}").buildAndExpand(savedDevice.getDeviceID()).toUri();

        return ResponseEntity.created(location).build();

    }


    /**
     * Retrieval of device details
     * @param deviceId
     * @return IDevice
     */
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<Device> details(@PathVariable UUID deviceId)
    {
        if(deviceId == null)
            throw new NullPointerException("Device id  cannot be null");

        return ResponseEntity.ok(new Device(deviceMemoryService.get(deviceId)));
    }


    /**
     * Client can set a single device status to OK or UNHEALTHY, but he must provide
     * secret key associated with the device as part of the request.
     * @param deviceId
     * @param device
     * @return IDevice
     */
    @PutMapping("/device/{deviceId}")
    public ResponseEntity<IDevice> updateStatus(@PathVariable UUID deviceId,@RequestBody Device device)
    {
        if(device.getDeviceStatus() == null || deviceId == null || StringUtils.isEmpty(device.getSecretKey()))
            throw new NullPointerException("Device id, device secret or status cannot be null");

        if(device.getDeviceStatus().equals(IDevice.Status.OK) || device.getDeviceStatus().equals(IDevice.Status.UNHEALTHY))
            return ResponseEntity.ok(deviceMemoryService.update(deviceId,device));

        throw new InvalidStatusUpdateException("Device status can only be updated to OK or UNHEALTHY");

    }

    /**
     * Returns list of devices with deviceName,and status of devices
     * and current status. secretkey should not be included
     * @return Collection<IDevice>
     */
    @GetMapping("/device")
    ResponseEntity<Collection<IDevice>> listAllDevices()
    {
        return ResponseEntity.ok(deviceMemoryService.getDeviceMAp().values());
    }


    /**
     * Same as {@code listAllDevices} but only devices with required status are returned.
     * @return Collection<IDevice>
     */
    @GetMapping("/device/status/{status}")
    ResponseEntity<Collection<IDevice>> listDevicesByStatus(@PathVariable IDevice.Status status)
    {
        if(ValidateStatus.validateStatus(status.name()))
            return ResponseEntity.ok(
                    deviceMemoryService.getDeviceMAp().values()
                            .stream()
                            .filter(device->device.getDeviceStatus().equals(status))
                            .collect(Collectors.toList()));

        throw new InvalidDeviceException("Device STATUS can be NEW,OK,UNHEALTHY,STALE");
    }

    /**
     * GraphQL query method to query alldevices, single device by id and status
     * @param query
     * @return
     */
    @PostMapping("/device/query")
    ResponseEntity<ExecutionResult> deviceQuery(@RequestBody String query)
    {
        ExecutionResult executionResult = graphQLService.getGraphQL().execute(query);
        return ResponseEntity.ok(executionResult);
    }


}
