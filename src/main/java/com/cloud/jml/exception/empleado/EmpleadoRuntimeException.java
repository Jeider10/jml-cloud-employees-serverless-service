package com.cloud.jml.exception.empleado;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmpleadoRuntimeException extends RuntimeException {

    private final HttpStatus status;

    public EmpleadoRuntimeException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
