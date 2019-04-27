package com.fs.dms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidDeviceException extends RuntimeException
{
    public InvalidDeviceException(String message) {
        super(message);
    }

    public InvalidDeviceException(String message, Throwable cause) {
        super(message, cause);
    }
}
