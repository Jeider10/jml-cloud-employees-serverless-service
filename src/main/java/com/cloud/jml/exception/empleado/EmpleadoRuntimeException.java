package com.cloud.jml.exception.empleado;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class EmpleadoRuntimeException extends RuntimeException {

    private final HttpStatus status;

    protected EmpleadoRuntimeException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    protected EmpleadoRuntimeException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
