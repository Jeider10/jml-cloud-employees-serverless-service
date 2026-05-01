package com.cloud.jml.repository;

import com.cloud.jml.model.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {
    Optional<EmpleadoEntity> findByIdentificacion(Long identificacion);

    List<EmpleadoEntity> findByNombres(String nombres);

    List<EmpleadoEntity> findByNombresContainingIgnoreCase(String nombres);

    List<EmpleadoEntity> findByApellidos(String apellidos);

    List<EmpleadoEntity> findByApellidosContainingIgnoreCase(String apellidos);

    List<EmpleadoEntity> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
}
