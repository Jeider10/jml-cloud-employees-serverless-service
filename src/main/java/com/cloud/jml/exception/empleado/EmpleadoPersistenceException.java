package com.cloud.jml.exception.empleado;

import org.springframework.http.HttpStatus;

public class EmpleadoPersistenceException extends EmpleadoRuntimeException {

    public EmpleadoPersistenceException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "💾 [PERSISTENCIA] " + message);
    }

    public EmpleadoPersistenceException(String message, Throwable cause) {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "💾 [PERSISTENCIA] " + message +
                        (cause != null ? " | 💥 Causa: " + cause.getMessage() : "")
        );
    }

    // 🔒 Violación de integridad (constraint, duplicado, etc.) al guardar
    public static EmpleadoPersistenceException integrityViolation(Throwable cause) {
        return new EmpleadoPersistenceException(
                "❌ [INTEGRIDAD] Violación de integridad en base de datos al guardar el empleado",
                cause
        );
    }

    // ⚙️ Error técnico de acceso a datos
    public static EmpleadoPersistenceException dataAccessError(Throwable cause) {
        return new EmpleadoPersistenceException(
                "❌ [DATOS] Error de acceso a datos al intentar guardar el empleado",
                cause
        );
    }

    // 💥 Error inesperado
    public static EmpleadoPersistenceException unexpected(Throwable cause) {
        return new EmpleadoPersistenceException(
                "💥 [INESPERADO] Ocurrió un error inesperado al registrar el empleado",
                cause
        );
    }
}
