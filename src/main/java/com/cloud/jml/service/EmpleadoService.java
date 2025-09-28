package com.cloud.jml.service;

import com.cloud.jml.dto.EmpleadoRequestDTO;
import com.cloud.jml.dto.EmpleadoResponseDTO;
import com.cloud.jml.exception.EmpleadoDuplicadoException;
import com.cloud.jml.exception.EmpleadoNoEncontradoException;
import com.cloud.jml.model.EmpleadoEntity;
import com.cloud.jml.repository.EmpleadoRepository;
import com.cloud.jml.utils.EmpleadoMapper;
import com.cloud.jml.utils.EmpleadoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper mapper;
    private final EmpleadoUtils empleadoUtils;

    public EmpleadoService(EmpleadoRepository empleadoRepository, EmpleadoMapper mapper, EmpleadoUtils empleadoUtils) {
        this.empleadoRepository = empleadoRepository;
        this.mapper = mapper;
        this.empleadoUtils = empleadoUtils;
        log.info("🔥 EmpleadoService inicializado correctamente.");
    }

    @Transactional
    public EmpleadoResponseDTO crearEmpleado(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("📌 Inicio de creación de Empleado: {}", empleadoRequestDTO.getNombres());

        // Verificar si ya existe por identificación
        Optional<EmpleadoEntity> byIdentificacion = empleadoRepository.findByIdentificacion(empleadoRequestDTO.getIdentificacion());

        if (byIdentificacion.isPresent()) {
            log.warn("⚠️ Empleado duplicado: {}", empleadoRequestDTO.getIdentificacion());
            throw new EmpleadoDuplicadoException(empleadoRequestDTO.getIdentificacion());
        }

        // Mapeo de DTO a Entity
        EmpleadoEntity empleadoEntity = mapper.mapRequestDtoToEntity(empleadoRequestDTO);

        // Guardamos en la base de datos
        EmpleadoEntity guardado = empleadoRepository.save(empleadoEntity);
        log.info("✅ Empleado guardado con Identificacion: {}", guardado.getIdentificacion());

        EmpleadoResponseDTO empleadoResponseDTO = mapper.mapEntityToResponseDto(guardado);
        log.info("📌 Finaliza creación de Empleado: {}", empleadoResponseDTO.getNombres());

        return empleadoResponseDTO;
    }

    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> listarEmpleados() {
        log.info("📌 Inicio de búsqueda de todos los Empleado");

        // Paso 1: Obtener entidades desde la BD
        List<EmpleadoEntity> empleadoEntity = empleadoRepository.findAll();

        // Paso 2: Convertir a Stream
        Stream<EmpleadoEntity> entityStream = empleadoEntity.stream();

        // Paso 3: Mapear cada entidad a DTO
        Stream<EmpleadoResponseDTO> streamDto = entityStream.map(mapper::mapEntityToResponseDto);

        // Paso 4: Convertir a lista final
        List<EmpleadoResponseDTO> empleadoResponse = streamDto.toList();

        log.info("📌 Finaliza búsqueda de todos los Empleado. Total encontrados: {}", empleadoResponse.size());

        return empleadoResponse;
    }

    @Transactional(readOnly = true)
    public Optional<EmpleadoResponseDTO> obtenerEmpleadoPorIdentificacion(Long identificacion) {
        log.info("📌 Inicio de búsqueda de Empleado por identificacion: {}", identificacion);

        Optional<EmpleadoEntity> optionalEmpleadoEntity = empleadoRepository.findByIdentificacion(identificacion);

        if (optionalEmpleadoEntity.isPresent()) {
            Optional<EmpleadoResponseDTO> proveedorResponseDTO = Optional.of(mapper.mapEntityToResponseDto(optionalEmpleadoEntity.get()));
            log.info("✅ Empleado encontrado con Identificación: {}", identificacion);
            return proveedorResponseDTO;
        } else {
            Optional<EmpleadoResponseDTO> responseDTO = Optional.empty();
            log.info("⚠️ Empleado no encontrado con Identificación: {}", identificacion);
            return responseDTO;
        }
    }

    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> obtenerEmpleadoPorNombres(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("📌 Inicio de búsqueda de Empleado por nombres: {}", empleadoRequestDTO.getNombres());

        // Paso 1: Buscar entidades por nombre
        List<EmpleadoEntity> empleadoEntity = empleadoRepository.findByNombresContainingIgnoreCase(empleadoRequestDTO.getNombres());

        // Paso 2: Validar si está vacío
        if (empleadoEntity.isEmpty()) {
            log.warn("⚠️ No se encontraron Empleado con nombre: {}", empleadoRequestDTO.getNombres());
            return List.of(); // Retorna lista vacía
        }

        // Paso 3: Convertir a Stream
        Stream<EmpleadoEntity> entityStream = empleadoEntity.stream();

        // Paso 4: Mapear cada entidad a DTO
        Stream<EmpleadoResponseDTO> streamDto = entityStream.map(mapper::mapEntityToResponseDto);

        // Paso 5: Convertir a lista final
        List<EmpleadoResponseDTO> empleadoResponse = streamDto.toList();

        log.info("📌 Finaliza búsqueda de Empleado por nombre: {}. Total encontrados: {}",
                empleadoRequestDTO.getNombres(), empleadoResponse.size());

        return empleadoResponse;
    }

    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> obtenerEmpleadoPorApellidos(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("📌 Inicio de búsqueda de Empleado por apellidos: {}", empleadoRequestDTO.getApellidos());

        // Paso 1: Buscar entidades por nombre
        List<EmpleadoEntity> empleadoEntity = empleadoRepository.findByApellidosContainingIgnoreCase(empleadoRequestDTO.getApellidos());

        // Paso 2: Validar si está vacío
        if (empleadoEntity.isEmpty()) {
            log.warn("⚠️ No se encontraron Empleado con apellidos: {}", empleadoRequestDTO.getApellidos());
            return List.of(); // Retorna lista vacía
        }

        // Paso 3: Convertir a Stream
        Stream<EmpleadoEntity> entityStream = empleadoEntity.stream();

        // Paso 4: Mapear cada entidad a DTO
        Stream<EmpleadoResponseDTO> streamDto = entityStream.map(mapper::mapEntityToResponseDto);

        // Paso 5: Convertir a lista final
        List<EmpleadoResponseDTO> empleadoResponse = streamDto.toList();

        log.info("📌 Finaliza búsqueda de Empleado por apellidos: {}. Total encontrados: {}",
                empleadoRequestDTO.getApellidos(), empleadoResponse.size());

        return empleadoResponse;
    }

    @Transactional
    public EmpleadoResponseDTO actualizarEmpleado(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("📌 Inicio de actualización de Empleado: {} con identificacion: {}",
                empleadoRequestDTO.getNombres(), empleadoRequestDTO.getIdentificacion());

        // Paso 1: Validar existencia
        EmpleadoEntity empleadoEntity = empleadoUtils.validarExistenciaCliente(empleadoRequestDTO);

        // Paso 2: Actualizar datos
        empleadoUtils.actualizarDatosEmpleado(empleadoRequestDTO, empleadoEntity);

        // Paso 3: Guardar cambios en la BD
        EmpleadoEntity actualizado = empleadoRepository.save(empleadoEntity);
        log.info("✅ Empleado actualizado con identificacion: {}", actualizado.getIdentificacion());

        // Paso 4: Mapear a DTO
        EmpleadoResponseDTO empleadoResponseDTO = mapper.mapEntityToResponseDto(actualizado);
        log.info("📌 Finaliza actualización de Empleado: {} con Identificación: {}",
                empleadoResponseDTO.getNombres(), empleadoResponseDTO.getIdentificacion());

        return empleadoResponseDTO;
    }

    @Transactional
    public void eliminarEmpleado(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("📌 Inicio de eliminación de Empleado con identificacion: {}", empleadoRequestDTO.getIdentificacion());

        Optional<EmpleadoEntity> empleadoOptional = empleadoRepository.findByIdentificacion(empleadoRequestDTO.getIdentificacion());

        if (empleadoOptional.isPresent()) {
            EmpleadoEntity empleadoEntity = empleadoOptional.get();
            empleadoRepository.delete(empleadoEntity);
            log.info("✅ Empleado eliminado con identificacion: {}", empleadoRequestDTO.getIdentificacion());
        } else {
            log.warn("⚠️ Empleado no encontrado con identificacion: {}", empleadoRequestDTO.getIdentificacion());
            throw new EmpleadoNoEncontradoException(empleadoRequestDTO.getIdentificacion());
        }
    }
}
