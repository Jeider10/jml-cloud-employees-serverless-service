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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public LocalDateTime parsearFechaInicio(String fecha) {
        log.info("📅 [FECHA] Intentando parsear fecha de inicio: {}", fecha);

        if (fecha == null || fecha.isBlank()) {
            log.error("⚠️ [ERROR] La fecha de inicio recibida es nula o vacia");
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }
        try {
            LocalDateTime res = LocalDateTime.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            log.info("✅ [PARSEADO] Fecha inicio procesada (Formato Completo): {}", res);
            return res;
        } catch (DateTimeParseException ignored) {}

        try {
            LocalDateTime res = LocalDateTime.parse(fecha);
            log.info("✅ [PARSEADO] Fecha inicio procesada (ISO): {}", res);
            return res;
        } catch (DateTimeParseException ignored) {}

        try {
            LocalDate localDate = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime res = localDate.atStartOfDay();
            log.info("✅ [PARSEADO] Fecha inicio procesada (Solo Fecha -> 00:00:00): {}", res);
            return res;
        } catch (DateTimeParseException e) {
            log.error("❌ [FORMATO INVALIDO] No se pudo parsear la fecha de inicio: {}", fecha);
            throw new IllegalArgumentException("Formato de fecha de inicio invalido. Use yyyy-MM-dd o yyyy-MM-dd HH:mm:ss");
        }
    }

    public LocalDateTime parsearFechaFin(String fecha) {
        log.info("📅 [FECHA] Intentando parsear fecha de fin: {}", fecha);

        if (fecha == null || fecha.isBlank()) {
            log.error("⚠️ [ERROR] La fecha de fin recibida es nula o vacia");
            throw new IllegalArgumentException("La fecha de fin es obligatoria");
        }
        try {
            LocalDateTime res = LocalDateTime.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            log.info("✅ [PARSEADO] Fecha fin procesada (Formato Completo): {}", res);
            return res;
        } catch (DateTimeParseException ignored) {}

        try {
            LocalDateTime res = LocalDateTime.parse(fecha);
            log.info("✅ [PARSEADO] Fecha fin procesada (ISO): {}", res);
            return res;
        } catch (DateTimeParseException ignored) {}

        try {
            LocalDate localDate = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime res = localDate.atTime(23, 59, 59);
            log.info("✅ [PARSEADO] Fecha fin procesada (Solo Fecha -> 23:59:59): {}", res);
            return res;
        } catch (DateTimeParseException e) {
            log.error("❌ [FORMATO INVALIDO] No se pudo parsear la fecha de fin: {}", fecha);
            throw new IllegalArgumentException("Formato de fecha de fin invalido. Use yyyy-MM-dd o yyyy-MM-dd HH:mm:ss");
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
