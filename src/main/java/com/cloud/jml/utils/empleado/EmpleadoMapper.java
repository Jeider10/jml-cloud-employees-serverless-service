package com.cloud.jml.utils.empleado;

import com.cloud.jml.dto.EmpleadoRequestDTO;
import com.cloud.jml.dto.EmpleadoResponseDTO;
import com.cloud.jml.model.EmpleadoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component // 🔹 Anotacion para indicar que es un componente de Spring
public class EmpleadoMapper {

    private final EmpleadoFormatearFecha empleadoFormatearFecha;

    public EmpleadoMapper(EmpleadoFormatearFecha empleadoFormatearFecha) {
        this.empleadoFormatearFecha = empleadoFormatearFecha;
        log.info("🔥 EmpleadoMapper inicializado correctamente.");
    }

    public EmpleadoEntity mapRequestDtoToEntity(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("📦 [MAPEO] Iniciando mapeo DTO → Entity para empleado");

        EmpleadoEntity empleadoEntity = new EmpleadoEntity();

        empleadoEntity.setIdentificacion(empleadoRequestDTO.getIdentificacion());
        empleadoEntity.setNombres(empleadoRequestDTO.getNombres());
        empleadoEntity.setApellidos(empleadoRequestDTO.getApellidos());
        empleadoEntity.setTelefono(empleadoRequestDTO.getTelefono());
        empleadoEntity.setDireccion(empleadoRequestDTO.getDireccion());
        empleadoEntity.setFechaCreacion(LocalDateTime.now());

        log.info("✅ [MAPEO] Mapeo completado DTO → Entity para empleado");

        return empleadoEntity;
    }

    public EmpleadoResponseDTO mapEntityToResponseDto(EmpleadoEntity empleadoEntity) {
        log.info("📦 [MAPEO] Iniciando mapeo Entity → DTO para empleado");

        EmpleadoResponseDTO empleadoResponseDTO = new EmpleadoResponseDTO();

        empleadoResponseDTO.setIdentificacion(empleadoEntity.getIdentificacion());
        empleadoResponseDTO.setNombres(empleadoEntity.getNombres());
        empleadoResponseDTO.setApellidos(empleadoEntity.getApellidos());
        empleadoResponseDTO.setTelefono(empleadoEntity.getTelefono());
        empleadoResponseDTO.setDireccion(empleadoEntity.getDireccion());

        // 🕓 Formateo de fechas
        empleadoFormatearFecha.asignarFechasFormateadas(empleadoEntity, empleadoResponseDTO);

        log.info("✅ [MAPEO] Mapeo completado Entity → DTO para empleado");

        return empleadoResponseDTO;
    }

    public void actualizarDatosEmpleadoExistente(EmpleadoRequestDTO empleadoRequestDTO, EmpleadoEntity empleadoEntity) {
        log.info("✏️ [SOLICITUD] Actualizando datos del empleado con identificacion: {}", empleadoEntity.getIdentificacion());

        // Actualizamos solo los campos permitidos
        empleadoEntity.setIdentificacion(empleadoRequestDTO.getIdentificacion());
        empleadoEntity.setNombres(empleadoRequestDTO.getNombres());
        empleadoEntity.setApellidos(empleadoRequestDTO.getApellidos());
        empleadoEntity.setTelefono(empleadoRequestDTO.getTelefono());
        empleadoEntity.setDireccion(empleadoRequestDTO.getDireccion());

        // Actualizamos la fecha de actualizacion
        empleadoEntity.setFechaActualizacion(LocalDateTime.now());

        log.info("✅ [FINALIZADO] Empleado actualizado correctamente: identificacion={}", empleadoEntity.getIdentificacion());
    }
}
