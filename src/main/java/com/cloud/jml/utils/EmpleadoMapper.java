package com.cloud.jml.utils;

import com.cloud.jml.dto.EmpleadoRequestDTO;
import com.cloud.jml.dto.EmpleadoResponseDTO;
import com.cloud.jml.model.EmpleadoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component // 🔹 Anotación para indicar que es un componente de Spring
public class EmpleadoMapper {

    private final EmpleadoUtils empleadoUtils;

    public EmpleadoMapper(EmpleadoUtils empleadoUtils) {
        this.empleadoUtils = empleadoUtils;
    }

    // ------------------ 🔹 Métodos de Mapeos ------------------

    public EmpleadoEntity mapRequestDtoToEntity(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("📌 Iniciando mapeo DTO a Entity para crear Empleado");

        EmpleadoEntity proveedorEntity = new EmpleadoEntity();

        proveedorEntity.setIdentificacion(empleadoRequestDTO.getIdentificacion());
        proveedorEntity.setNombres(empleadoRequestDTO.getNombres());
        proveedorEntity.setApellidos(empleadoRequestDTO.getApellidos());
        proveedorEntity.setTelefono(empleadoRequestDTO.getTelefono());
        proveedorEntity.setDireccion(empleadoRequestDTO.getDireccion());
        proveedorEntity.setFechaCreacion(LocalDateTime.now());

        log.info("📌 Finalizando mapeo DTO a Entity para crear Empleado");

        return proveedorEntity;
    }

    public EmpleadoResponseDTO mapEntityToResponseDto(EmpleadoEntity proveedorEntity) {
        log.info("📌 Iniciando mapeo Entity a DTO para crear Empleado");

        EmpleadoResponseDTO proveedorResponseDTO = new EmpleadoResponseDTO();

        proveedorResponseDTO.setIdentificacion(proveedorEntity.getIdentificacion());
        proveedorResponseDTO.setNombres(proveedorEntity.getNombres());
        proveedorResponseDTO.setApellidos(proveedorEntity.getApellidos());
        proveedorResponseDTO.setTelefono(proveedorEntity.getTelefono());
        proveedorResponseDTO.setDireccion(proveedorEntity.getDireccion());

        // 🔹 Formatear fechas
        empleadoUtils.asignarFechasFormateadas(proveedorEntity, proveedorResponseDTO);

        log.info("📌 Finalizando mapeo Entity a DTO para crear Empleado");

        return proveedorResponseDTO;
    }
}
