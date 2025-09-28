package com.cloud.jml.exception;

public class EmpleadoDuplicadoException extends RuntimeException {

    public EmpleadoDuplicadoException(Long identificacion) {
        super("El Empleado con identificación " + identificacion + " ya existe.");
    }
}
