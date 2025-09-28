package com.cloud.jml.exception;

public class EmpleadoNoEncontradoException extends RuntimeException {

    public EmpleadoNoEncontradoException(Long identificacion) {
        super("No se encontró Empleado con identificación: " + identificacion);
    }
}
