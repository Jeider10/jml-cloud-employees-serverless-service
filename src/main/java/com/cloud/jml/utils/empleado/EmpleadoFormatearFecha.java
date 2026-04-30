package com.cloud.jml.utils.empleado;

import com.cloud.jml.dto.EmpleadoResponseDTO;
import com.cloud.jml.model.EmpleadoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Component // 🔹 Anotacion para indicar que es un componente de Spring
public class EmpleadoFormatearFecha {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("d/M/yyyy, h:mm:ss a", Locale.of("es", "CO"));

    public EmpleadoFormatearFecha() {
        log.info("🔥 EmpleadoFormatearFecha inicializado correctamente.");
    }

    public void asignarFechasFormateadas(EmpleadoEntity empleadoEntity, EmpleadoResponseDTO empleadoResponseDTO) {
        if (empleadoEntity == null || empleadoResponseDTO == null) {
            log.warn("⚠️ Entidad o DTO nulos al intentar asignar fechas formateadas.");
            return;
        }

        log.info("📦 Asignando fechas formateadas al empleado: {}", empleadoEntity.getNombres());

        // Fecha de creacion
        String fechaCreacion = formatearFecha(empleadoEntity.getFechaCreacion());
        empleadoResponseDTO.setFechaCreacion(fechaCreacion);
        log.debug("🕓 Fecha de creacion asignada: {}", fechaCreacion);

        // Fecha de actualizacion
        String fechaActualizacion = formatearFecha(empleadoEntity.getFechaActualizacion());
        empleadoResponseDTO.setFechaActualizacion(fechaActualizacion);
        log.debug("🕓 Fecha de actualizacion asignada: {}", fechaActualizacion);
    }

    /**
     * 🕒 Formatea una fecha LocalDateTime al formato colombiano:
     * Ejemplo → 18/10/2025, 2:35:45 p.m.
     */
    public String formatearFecha(LocalDateTime fecha) {
        if (fecha == null) {
            log.warn("⚠️ Fecha recibida nula, se retorna null.");
            return null;
        }

        String fechaFormateada = fecha.format(FORMATTER).toLowerCase();
        log.info("🕓 Formateando fecha: {}", fechaFormateada);

        // Reemplazar expresiones locales de AM/PM con formato limpio
        fechaFormateada = fechaFormateada
                .replace("a. m.", "a.m.")
                .replace("p. m.", "p.m.");

        log.info("🕓 Fecha formateada correctamente: {}", fechaFormateada);

        return fechaFormateada;
    }
}
