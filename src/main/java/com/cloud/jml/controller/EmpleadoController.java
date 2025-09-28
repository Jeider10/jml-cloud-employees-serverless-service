package com.cloud.jml.controller;

import com.cloud.jml.dto.EmpleadoRequestDTO;
import com.cloud.jml.dto.EmpleadoResponseDTO;
import com.cloud.jml.exception.EmpleadoNoEncontradoException;
import com.cloud.jml.service.EmpleadoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/empleados")
@CrossOrigin(origins = "http://localhost:8080")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @PostMapping("/register")
    public ResponseEntity<EmpleadoResponseDTO> crearEmpleado(@RequestBody EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("📌 Iniciando petición para crear Empleado: {}", empleadoRequestDTO.getNombres());

        EmpleadoResponseDTO response = empleadoService.crearEmpleado(empleadoRequestDTO);

        log.info("📌 Finaliza petición para crear Empleado: {}", empleadoRequestDTO.getNombres());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar-todos")
    public ResponseEntity<List<EmpleadoResponseDTO>> listarEmpleados() {
        log.info("📌 Iniciando petición para listar todos los Empleados");

        List<EmpleadoResponseDTO> empleados = empleadoService.listarEmpleados();

        log.info("📌 Finaliza petición para listar todos los Empleados");

        return ResponseEntity.ok(empleados);
    }

    @GetMapping("/identificacion")
    public ResponseEntity<EmpleadoResponseDTO> obtenerEmpleadoPorIdentificacion(@RequestParam("identificacion") Long identificacion) {
        log.info("📌 Iniciando petición para buscar Empleado por identificacion: {}", identificacion);

        Optional<EmpleadoResponseDTO> response = empleadoService.obtenerEmpleadoPorIdentificacion(identificacion);

        ResponseEntity<EmpleadoResponseDTO> empleadoResponse;

        if (response.isPresent()) {
            empleadoResponse = ResponseEntity.ok(response.get());
            log.info("✅ Empleado encontrado con identificacion: {}", identificacion);
        } else {
            empleadoResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            log.warn("❌ Empleado no encontrado con identificacion: {}", identificacion);
        }

        log.info("📌 Finaliza petición de buscar Empleado por identificacion: {}", identificacion);

        return empleadoResponse;
    }

    @GetMapping("/nombres")
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerEmpleadoPorNombres(@RequestParam("nombres") String nombres) {
        log.info("📌 Iniciando petición para buscar Empleado por nombres: {}", nombres);

        EmpleadoRequestDTO empleadoRequestDTO = new EmpleadoRequestDTO();
        empleadoRequestDTO.setNombres(nombres);

        List<EmpleadoResponseDTO> response = empleadoService.obtenerEmpleadoPorNombres(empleadoRequestDTO);

        ResponseEntity<List<EmpleadoResponseDTO>> empleadoResponse;

        if (response.isEmpty()) {
            empleadoResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            log.warn("❌ No se encontraron Empleado con nombre: {}", nombres);
        } else {
            empleadoResponse = ResponseEntity.ok(response);
            log.info("✅ Empleado encontrados con nombre: {}", nombres);
        }

        log.info("📌 Finaliza petición de buscar Empleado por nombres: {}", nombres);

        return empleadoResponse;
    }

    @GetMapping("/apellidos")
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerEmpleadoPorApellidos(@RequestParam("apellidos") String apellidos) {
        log.info("📌 Iniciando petición para buscar Empleado por apellidos: {}", apellidos);

        EmpleadoRequestDTO empleadoRequestDTO = new EmpleadoRequestDTO();
        empleadoRequestDTO.setApellidos(apellidos);

        List<EmpleadoResponseDTO> response = empleadoService.obtenerEmpleadoPorApellidos(empleadoRequestDTO);

        ResponseEntity<List<EmpleadoResponseDTO>> empleadoResponse;

        if (response.isEmpty()) {
            empleadoResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            log.warn("❌ No se encontraron Empleado con apellidos: {}", apellidos);
        } else {
            empleadoResponse = ResponseEntity.ok(response);
            log.info("✅ Empleado encontrados con apellidos: {}", apellidos);
        }

        log.info("📌 Finaliza petición de buscar Empleado por apellidos: {}", apellidos);

        return empleadoResponse;
    }

    @PutMapping("/actualizar")
    public ResponseEntity<EmpleadoResponseDTO> actualizarEmpleado(@RequestBody EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("📌 Iniciando petición para actualizar Empleado con identificacion: {}", empleadoRequestDTO.getIdentificacion());

        EmpleadoResponseDTO response;

        try {
            response = empleadoService.actualizarEmpleado(empleadoRequestDTO);
            log.info("📌 Finaliza petición de actualización de Empleado con identificacion: {}", empleadoRequestDTO.getIdentificacion());
            return ResponseEntity.ok(response);
        } catch (EmpleadoNoEncontradoException ex) {
            log.warn("❌ No se pudo actualizar el Empleado. Identificacion no encontrado: {}", empleadoRequestDTO.getIdentificacion());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/eliminar-identificacion")
    public ResponseEntity<Void> eliminarEmpleado(@RequestParam("identificacion") Long identificacion) {
        log.info("📌 Iniciando petición para eliminar Empleado con identificacion: {}", identificacion);

        EmpleadoRequestDTO empleadoRequestDTO = new EmpleadoRequestDTO();
        empleadoRequestDTO.setIdentificacion(identificacion);

        try {
            empleadoService.eliminarEmpleado(empleadoRequestDTO);
            log.info("📌 Finalizó petición de eliminación de Empleado con identificacion: {}", identificacion);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.warn("⚠️ Error al eliminar Empleado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
