package com.cloud.jml.exception.empleado;

import org.springframework.http.HttpStatus;

public class EmpleadoNoEncontradoException extends EmpleadoRuntimeException {

    public EmpleadoNoEncontradoException(Long identificacion) {
        super(
                HttpStatus.NOT_FOUND,
                "❌ [CONSULTA] Empleado no encontrado con identificación: " + identificacion);
    }
}
