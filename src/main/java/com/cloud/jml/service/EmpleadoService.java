package com.cloud.jml.service;

import com.cloud.jml.dto.EmpleadoRequestDTO;
import com.cloud.jml.dto.EmpleadoResponseDTO;
import com.cloud.jml.exception.empleado.EmpleadoDuplicadoException;
import com.cloud.jml.exception.empleado.EmpleadoNoEncontradoException;
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

    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> listarEmpleados() {
        log.info("🔍 [CONSULTA] Recuperando todos los empleados desde la base de datos");

        List<EmpleadoEntity> empleadoEntity = empleadoRepository.findAll();

        if (empleadoEntity.isEmpty()) {
            log.warn("⚠️ [RESULTADO] No se encontraron empleados registradas en la base de datos");
            return List.of();
        }

        log.info("📦 [MAPEO] Transformando {} entidades de empleados a DTOs", empleadoEntity.size());

        // convertir a stream
        Stream<EmpleadoEntity> entityStream = empleadoEntity.stream();

        // mapear entidades a DTOs
        Stream<EmpleadoResponseDTO> streamDto = entityStream.map(mapper::mapEntityToResponseDto);

        // recolectar en lista
        List<EmpleadoResponseDTO> empleadoResponse = streamDto.toList();

        log.info("✅ [FINALIZADO] Total de empleados mapeados y retornados: {}", empleadoResponse.size());

        return empleadoResponse;
    }

    @Transactional
    public EmpleadoResponseDTO crearEmpleado(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("🔍 [CONSULTA] Inicio de creación de empleado: {}", empleadoRequestDTO.getNombres());

        Optional<EmpleadoEntity> empleadoExistente = empleadoRepository.findByIdentificacion(empleadoRequestDTO.getIdentificacion());

        if (empleadoExistente.isPresent()) {
            log.warn("❌ [ERROR] Empleado duplicado detectado: {}", empleadoRequestDTO.getIdentificacion());
            throw new EmpleadoDuplicadoException(empleadoRequestDTO.getIdentificacion());
        }

        log.info("📦 [MAPEO] Transformando DTO a entidad de empleado");
        EmpleadoEntity empleadoEntity = mapper.mapRequestDtoToEntity(empleadoRequestDTO);
        log.info("📦 [MAPEO] Empleado: {} mapeado a entidad con identificación: {}", empleadoEntity.getNombres(), empleadoEntity.getIdentificacion());

        EmpleadoEntity guardarEmpleado = empleadoRepository.save(empleadoEntity);
        log.info("💾 [PERSISTENCIA] Empleado: {} guardado exitosamente con identificación: {}", guardarEmpleado.getNombres(), guardarEmpleado.getIdentificacion());

        log.info("📦 [MAPEO] Transformando entidad de producto a DTO. (crearEmpleado)");
        EmpleadoResponseDTO empleadoResponseDTO = mapper.mapEntityToResponseDto(guardarEmpleado);
        log.info("📦 [MAPEO] Empleado mapeado a DTO. nombres: {}, apellidos: {}, identificación: {}",
                empleadoResponseDTO.getNombres(), empleadoResponseDTO.getApellidos(), empleadoResponseDTO.getIdentificacion());

        log.info("✅ [FINALIZADO] Empleado creado correctamente: {} con identificación {}", empleadoResponseDTO.getNombres(), empleadoResponseDTO.getIdentificacion());

        return empleadoResponseDTO;
    }

    @Transactional(readOnly = true)
    public EmpleadoResponseDTO obtenerEmpleadoPorIdentificacion(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("🔍 [CONSULTA] Iniciando búsqueda de empleado por identificación: {}", empleadoRequestDTO.getIdentificacion());

        Optional<EmpleadoEntity> optionalEmpleado = empleadoRepository.findByIdentificacion(empleadoRequestDTO.getIdentificacion());

        if (optionalEmpleado.isEmpty()) {
            log.warn("❌ [RESULTADO] Empleado no encontrado con identificación: {}", empleadoRequestDTO.getIdentificacion());
            return null;
        }

        EmpleadoEntity empleadoEntity = optionalEmpleado.get();
        log.info("📦 [ENCONTRADO] Empleado encontrado -> nombres: {}, apellidos: {}, identificación: {}",
                empleadoEntity.getNombres(), empleadoEntity.getApellidos(), empleadoEntity.getIdentificacion());

        log.info("📦 [MAPEO] Transformando entidad de empleado a DTO. (obtenerEmpleadoPorIdentificacion)");
        EmpleadoResponseDTO empleadoResponseDTO = mapper.mapEntityToResponseDto(empleadoEntity);
        log.info("📦 [MAPEO] Empleado mapeado a DTO. identificación: {}", empleadoResponseDTO.getIdentificacion());

        log.info("✅ [FINALIZADO] Empleado encontrado con identificación: {}", empleadoResponseDTO.getIdentificacion());

        return empleadoResponseDTO;
    }

    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> obtenerEmpleadoPorNombres(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("🔍 [CONSULTA] Iniciando búsqueda de empleados por nombre: {}", empleadoRequestDTO.getNombres());

        List<EmpleadoEntity> empleadoEntity = empleadoRepository.findByNombresContainingIgnoreCase(empleadoRequestDTO.getNombres());

        if (empleadoEntity.isEmpty()) {
            log.warn("❌ [RESULTADO] No se encontraron empleados con nombre: {}", empleadoRequestDTO.getNombres());
            return List.of();
        }

        log.info("📦 [MAPEO] Transformando {} entidades de empleados a DTOs (nombre: {})", empleadoEntity.size(), empleadoRequestDTO.getNombres());

        // convertir a stream
        Stream<EmpleadoEntity> entityStream = empleadoEntity.stream();

        // mapear entidades a DTOs
        Stream<EmpleadoResponseDTO> streamDto = entityStream.map(mapper::mapEntityToResponseDto);

        // recolectar en lista
        List<EmpleadoResponseDTO> empleadoResponse = streamDto.toList();

        log.info("✅ [FINALIZADO] Empleados encontrados con nombre: {}. Total encontrados: {}", empleadoRequestDTO.getNombres(), empleadoResponse.size());

        return empleadoResponse;
    }

    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> obtenerEmpleadoPorApellidos(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("🔍 [CONSULTA] Iniciando búsqueda de empleado por apellido: {}", empleadoRequestDTO.getApellidos());

        List<EmpleadoEntity> empleadoEntity = empleadoRepository.findByApellidosContainingIgnoreCase(empleadoRequestDTO.getApellidos());

        // Paso 2: Validar si está vacío
        if (empleadoEntity.isEmpty()) {
            log.warn("❌ [RESULTADO] No se encontraron empleados con apellido: {}", empleadoRequestDTO.getApellidos());
            return List.of();
        }

        log.info("📦 [MAPEO] Transformando {} entidades de empleados a DTOs (apellido: {})", empleadoEntity.size(), empleadoRequestDTO.getApellidos());

        // convertir a stream
        Stream<EmpleadoEntity> entityStream = empleadoEntity.stream();

        // mapear entidades a DTOs
        Stream<EmpleadoResponseDTO> streamDto = entityStream.map(mapper::mapEntityToResponseDto);

        // recolectar en lista
        List<EmpleadoResponseDTO> empleadoResponse = streamDto.toList();

        log.info("✅ [FINALIZADO] Empleados encontrados con apellido: {}. Total encontrados: {}", empleadoRequestDTO.getApellidos(), empleadoResponse.size());

        return empleadoResponse;
    }

    @Transactional
    public EmpleadoResponseDTO actualizarEmpleado(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("🔍 [CONSULTA] Inicio de actualización de empleado: {} con identificación: {}",
                empleadoRequestDTO.getNombres(), empleadoRequestDTO.getIdentificacion());

        // Paso 1: Validar existencia
        EmpleadoEntity empleadoEntity = empleadoUtils.validarExistenciaEmpleado(empleadoRequestDTO);

        // Paso 2: Actualizar datos
        mapper.actualizarDatosEmpleadoExistente(empleadoRequestDTO, empleadoEntity);

        // Paso 3: Guardar cambios en la BD
        EmpleadoEntity actualizado = empleadoRepository.save(empleadoEntity);
        log.info("💾 [PERSISTENCIA] Empleado actualizado: {} con identificación: {}", actualizado.getNombres(), actualizado.getIdentificacion());

        // Paso 4: Mapear a DTO
        log.info("📦 [MAPEO] Transformando entidad de empleado a DTO. (actualizarEmpleado)");
        EmpleadoResponseDTO empleadoResponseDTO = mapper.mapEntityToResponseDto(actualizado);
        log.info("📦 [MAPEO] Empleado mapeado a DTO. identificación: {}, nombres: {}",
                empleadoResponseDTO.getIdentificacion(), empleadoResponseDTO.getNombres());

        log.info("✅ [FINALIZADO] Actualización de empleado completada: {} con identificación: {}", empleadoResponseDTO.getNombres(), empleadoResponseDTO.getIdentificacion());

        return empleadoResponseDTO;
    }

    @Transactional
    public void eliminarEmpleado(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("🔍 [CONSULTA] Inicio de eliminación de empleado con identificación: {}", empleadoRequestDTO.getIdentificacion());

        Optional<EmpleadoEntity> empleadoExistente = empleadoRepository.findByIdentificacion(empleadoRequestDTO.getIdentificacion());

        if (empleadoExistente.isPresent()) {
            EmpleadoEntity empleadoEntity = empleadoExistente.get();
            log.info("📦 [ENCONTRADO] Empleado localizado -> {} con identificación: {}", empleadoEntity.getNombres(), empleadoEntity.getIdentificacion());

            empleadoRepository.delete(empleadoEntity);
            log.info("🗑️ [ELIMINADO] Empleado eliminado correctamente -> {} con identificación: {}", empleadoEntity.getNombres(), empleadoEntity.getIdentificacion());
        } else {
            log.warn("❌ [NO ENCONTRADO] Empleado no encontrado con identificación: {}", empleadoRequestDTO.getIdentificacion());
            throw new EmpleadoNoEncontradoException(empleadoRequestDTO.getIdentificacion());
        }
    }
}
