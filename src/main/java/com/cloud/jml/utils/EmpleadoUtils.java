package com.cloud.jml.utils;

import com.cloud.jml.dto.EmpleadoRequestDTO;
import com.cloud.jml.dto.EmpleadoResponseDTO;
import com.cloud.jml.exception.EmpleadoNoEncontradoException;
import com.cloud.jml.model.EmpleadoEntity;
import com.cloud.jml.repository.EmpleadoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Component // 🔹 Anotación para indicar que es un componente de Spring
public class EmpleadoUtils {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("d/M/yyyy, h:mm:ss a", Locale.of("es", "CO"));

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoUtils(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
        log.info("🔥 EmpleadoUtils inicializado correctamente.");
    }

    public EmpleadoEntity validarExistenciaCliente(EmpleadoRequestDTO empleadoRequestDTO) {
        Optional<EmpleadoEntity> optionalEmpleado = empleadoRepository.findByIdentificacion(empleadoRequestDTO.getIdentificacion());

        if (optionalEmpleado.isPresent()) {
            log.info("📌 Empleado encontrado con Identificación: {}", empleadoRequestDTO.getIdentificacion());
            return optionalEmpleado.get();
        } else {
            log.warn("⚠️ Empleado no encontrado con Identificación: {}", empleadoRequestDTO.getIdentificacion());
            throw new EmpleadoNoEncontradoException(empleadoRequestDTO.getIdentificacion());
        }
    }

    public void actualizarDatosEmpleado(EmpleadoRequestDTO empleadoRequestDTO, EmpleadoEntity proveedorEntity) {
        // Actualizamos solo los campos permitidos
        proveedorEntity.setIdentificacion(empleadoRequestDTO.getIdentificacion());
        proveedorEntity.setNombres(empleadoRequestDTO.getNombres());
        proveedorEntity.setApellidos(empleadoRequestDTO.getApellidos());
        proveedorEntity.setTelefono(empleadoRequestDTO.getTelefono());
        proveedorEntity.setDireccion(empleadoRequestDTO.getDireccion());

        // Actualizamos la fecha de actualización
        proveedorEntity.setFechaActualizacion(LocalDateTime.now());
    }

    public String formatearFecha(LocalDateTime fecha) {
        String fechaFormateada = fecha.format(FORMATTER).toLowerCase();
        log.info("📌 Fecha formateada originalmente: {}", fechaFormateada);

        // Reemplazar y reasignar el valor "a. m." → "a.m." y "p. m." → "p.m."
        fechaFormateada = fechaFormateada
                .replace("a. m.", "a.m.")
                .replace("p. m.", "p.m.");

        log.info("📌 Fecha formateada final: {}", fechaFormateada);

        return fechaFormateada;
    }

    public void asignarFechasFormateadas(EmpleadoEntity proveedorEntity, EmpleadoResponseDTO empleadoResponseDTO) {
        if (proveedorEntity.getFechaCreacion() != null) {
            String fechaCreacion = formatearFecha(proveedorEntity.getFechaCreacion());
            log.info("📌 Fecha creación formateada: {}", fechaCreacion);

            empleadoResponseDTO.setFechaCreacion(fechaCreacion);
        } else {
            empleadoResponseDTO.setFechaCreacion(null);
        }

        if (proveedorEntity.getFechaActualizacion() != null) {
            String fechaActualizacion = formatearFecha(proveedorEntity.getFechaActualizacion());
            log.info("📌 Fecha actualización formateada: {}", fechaActualizacion);

            empleadoResponseDTO.setFechaActualizacion(fechaActualizacion);
        } else {
            empleadoResponseDTO.setFechaActualizacion(null);
        }
    }
}
