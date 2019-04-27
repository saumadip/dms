package com.fs.dms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidStatusUpdateException extends RuntimeException
{
    public InvalidStatusUpdateException() {
    }

    public InvalidStatusUpdateException(String message) {
        super(message);
    }

    public InvalidStatusUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
