Device Manager REST service
==============================================

Create a simple REST (or GraphQL) service (no user interface required) for 
tracking status of devices.

Use cases
---------

The service should support following use cases:

Device registration
    Each device has an associated human-friendly name, secret key and status.
    Client provides name and secret key, while the status is initially set to
    NEW. Neither name nor secret key are assumed to be unique.

Retrieval of device details
    Returns name, secret key and current status of a single device.

Listing devices
    Includes names and status of devices, but not secret keys

Device status update
    Client can set a single device status to OK or UNHEALTHY, but he must provide 
    secret key associated with the device as part of the request. Otherwise 
    update attempt is rejected.

Automatic device status update
    If some device status is OK, but it has not been changed by API call during
    last 5 minutes, than the device status changes to STALE.

Listing devices by status
    Same as 'Listing devices' but only devices with required status are returned.

Note:

- Please follow REST API design best practices.

- No authentication/authorization is required for making API calls.

- Plain HTTP is enough.

- Restarting application should cause loss of data, do not use external
  persistence layer (database, in-memory database, Hibernate etc).
  
