package com.cloud.jml.exception.empleado;

import org.springframework.http.HttpStatus;

public class EmpleadoDeletionException extends EmpleadoRuntimeException {

    public EmpleadoDeletionException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "🗑️ [ELIMINACION] " + message);
    }

    public EmpleadoDeletionException(String message, Throwable cause) {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "🗑️ [ELIMINACION] " + message +
                        (cause != null ? " | 💥 Causa: " + cause.getMessage() : "")
        );
    }

    // 🔒 Violacion de integridad referencial (por constraints o dependencias)
    public static EmpleadoDeletionException integrityViolation(Throwable cause) {
        return new EmpleadoDeletionException(
                "❌ [INTEGRIDAD] No se pudo eliminar el empleado debido a una violacion de integridad referencial",
                cause
        );
    }

    // ⚙️ Error de acceso a datos
    public static EmpleadoDeletionException dataAccessError(Throwable cause) {
        return new EmpleadoDeletionException(
                "❌ [DATOS] Error de acceso a la base de datos al intentar eliminar el empleado",
                cause
        );
    }

    // 💥 Error inesperado
    public static EmpleadoDeletionException unexpected(Throwable cause) {
        return new EmpleadoDeletionException(
                "💥 [INESPERADO] Ocurrio un error inesperado al intentar eliminar el empleado",
                cause
        );
    }
}
