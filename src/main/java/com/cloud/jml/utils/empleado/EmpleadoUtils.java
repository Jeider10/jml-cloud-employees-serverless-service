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
@Component // 🔹 Anotacion para indicar que es un componente de Spring
public class EmpleadoUtils {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoUtils(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
        log.info("🔥 EmpleadoUtils inicializado correctamente.");
    }

    public EmpleadoEntity validarExistenciaEmpleado(EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("🔍 [SOLICITUD] Validando existencia de empleado: {} con identificacion: {}", empleadoRequestDTO.getNombres(), empleadoRequestDTO.getIdentificacion());
        Optional<EmpleadoEntity> optionalEmpleado = empleadoRepository.findByIdentificacion(empleadoRequestDTO.getIdentificacion());

        if (optionalEmpleado.isPresent()) {
            EmpleadoEntity empleadoEntity = optionalEmpleado.get();
            log.info("✅ [FINALIZADO] Empleado encontrado con identificacion: {}", empleadoEntity.getIdentificacion());
            return empleadoEntity;
        } else {
            log.warn("⚠️ [RESULTADO] Empleado no encontrado con identificacion: {}", empleadoRequestDTO.getIdentificacion());
            throw new EmpleadoNoEncontradoException(empleadoRequestDTO.getIdentificacion());
        }
    }

    public EmpleadoEntity guardarEmpleadoBD(EmpleadoEntity empleadoEntity) {
        try {
            return empleadoRepository.save(empleadoEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("🚨 Violacion de integridad al guardar el empleado: {}", e.getMessage(), e);
            throw EmpleadoPersistenceException.integrityViolation(e);

        } catch (DataAccessException e) {
            log.error("🚨 Error de acceso a datos al guardar el empleado: {}", e.getMessage(), e);
            throw EmpleadoPersistenceException.dataAccessError(e);

        } catch (Exception e) {
            log.error("🚨 Error inesperado al guardar el empleado: {}", e.getMessage(), e);
            throw EmpleadoPersistenceException.unexpected(e);
        }
    }

    public void eliminarEmpleadoBD(EmpleadoEntity empleadoEntity) {
        try {
            empleadoRepository.delete(empleadoEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("🚨 Violacion de integridad al eliminar el empleado: {}", e.getMessage(), e);
            throw EmpleadoDeletionException.integrityViolation(e);

        } catch (DataAccessException e) {
            log.error("🚨 Error de acceso a datos al eliminar el empleado: {}", e.getMessage(), e);
            throw EmpleadoDeletionException.dataAccessError(e);

        } catch (Exception e) {
            log.error("🚨 Error inesperado al eliminar el empleado: {}", e.getMessage(), e);
            throw EmpleadoDeletionException.unexpected(e);
        }
    }
}
