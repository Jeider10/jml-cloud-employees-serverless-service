package com.cloud.jml.utils.empleado;

import com.cloud.jml.dto.EmpleadoRequestDTO;
import com.cloud.jml.exception.empleado.EmpleadoDeletionException;
import com.cloud.jml.exception.empleado.EmpleadoNoEncontradoException;
import com.cloud.jml.exception.empleado.EmpleadoPersistenceException;
import com.cloud.jml.model.EmpleadoEntity;
import com.cloud.jml.repository.EmpleadoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component // 🔹 Anotación para indicar que es un componente de Spring
public class EmpleadoUtils {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoUtils(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
        log.info("🔥 EmpleadoUtils inicializado correctamente.");
    }

    public EmpleadoEntity validarExistenciaEmpleado(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("🔍 [SOLICITUD] Validando existencia de empleado: {} con identificación: {}", empleadoRequestDTO.getNombres(), empleadoRequestDTO.getIdentificacion());
        Optional<EmpleadoEntity> optionalEmpleado = empleadoRepository.findByIdentificacion(empleadoRequestDTO.getIdentificacion());

        if (optionalEmpleado.isPresent()) {
            EmpleadoEntity empleadoEntity = optionalEmpleado.get();
            log.info("✅ [FINALIZADO] Empleado encontrado con identificación: {}", empleadoEntity.getIdentificacion());
            return empleadoEntity;
        } else {
            log.warn("⚠️ [RESULTADO] Empleado no encontrado con identificación: {}", empleadoRequestDTO.getIdentificacion());
            throw new EmpleadoNoEncontradoException(empleadoRequestDTO.getIdentificacion());
        }
    }

    public EmpleadoEntity guardarEmpleadoBD(EmpleadoEntity empleadoEntity) {
        try {
            return empleadoRepository.save(empleadoEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("🚨 Violación de integridad al guardar el empleado: {}", e.getMessage(), e);
            throw new EmpleadoPersistenceException("Error de integridad en base de datos al guardar el empleado", e);

        } catch (DataAccessException e) {
            log.error("🚨 Error de acceso a datos al guardar el empleado: {}", e.getMessage(), e);
            throw new EmpleadoPersistenceException("Error al guardar el empleado en la base de datos", e);

        } catch (Exception e) {
            log.error("🚨 Error inesperado al guardar el empleado: {}", e.getMessage(), e);
            throw new EmpleadoPersistenceException("Error inesperado al registrar el empleado", e);
        }
    }

    public void eliminarEmpleadoBD(EmpleadoEntity empleadoEntity) {
        try {
            empleadoRepository.delete(empleadoEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("🚨 Violación de integridad al eliminar el empleado: {}", e.getMessage(), e);
            throw new EmpleadoDeletionException("Error de integridad en base de datos al eliminar el empleado", e);

        } catch (DataAccessException e) {
            log.error("🚨 Error de acceso a datos al eliminar el empleado: {}", e.getMessage(), e);
            throw new EmpleadoDeletionException("Error al eliminar el empleado en la base de datos", e);

        } catch (Exception e) {
            log.error("🚨 Error inesperado al eliminar el empleado: {}", e.getMessage(), e);
            throw new EmpleadoDeletionException("Error inesperado al eliminar el empleado", e);
        }
    }
}
