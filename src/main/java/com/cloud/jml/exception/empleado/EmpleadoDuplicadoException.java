package com.cloud.jml.exception.empleado;

import org.springframework.http.HttpStatus;

public class EmpleadoDuplicadoException extends EmpleadoRuntimeException {

    public EmpleadoDuplicadoException(Long identificacion) {
        super(
                HttpStatus.CONFLICT,
                "⚠️ [DUPLICADO] Empleado duplicado detectado con identificacion: " + identificacion);
    }
}
