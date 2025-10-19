package com.cloud.jml.controller;

import com.cloud.jml.dto.EmpleadoRequestDTO;
import com.cloud.jml.dto.EmpleadoResponseDTO;
import com.cloud.jml.service.EmpleadoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/empleados")
@CrossOrigin(origins = "http://localhost:8080")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
        log.info("🔥 EmpleadoController inicializado correctamente.");
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<EmpleadoResponseDTO>> listarEmpleados() {
        log.info("📥 [SOLICITUD] Listar todos los empleados");

        List<EmpleadoResponseDTO> empleados = empleadoService.listarEmpleados();

        log.info("📤 [RESPUESTA] Se retornan {} empleados", empleados.size());

        return ResponseEntity.ok(empleados);
    }

    @PostMapping("/register")
    public ResponseEntity<EmpleadoResponseDTO> crearEmpleado(@RequestBody EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("📥 [SOLICITUD] Crear empleado: {}", empleadoRequestDTO.getNombres());

        EmpleadoResponseDTO response = empleadoService.crearEmpleado(empleadoRequestDTO);

        log.info("📤 [RESPUESTA] Empleado creado: {} con identificación: {}", response.getNombres(), response.getIdentificacion());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/identificacion")
    public ResponseEntity<EmpleadoResponseDTO> obtenerEmpleadoPorIdentificacion(@RequestParam("identificacion") Long identificacion) {
        log.info("🔍 [SOLICITUD] Buscar empleado por identificación: {}", identificacion);

        EmpleadoRequestDTO empleadoRequestDTO = new EmpleadoRequestDTO();
        empleadoRequestDTO.setIdentificacion(identificacion);

        EmpleadoResponseDTO empleadoIdentificacion = empleadoService.obtenerEmpleadoPorIdentificacion(empleadoRequestDTO);

        log.info("📤 [RESPUESTA] Empleado encontrado con identificación: {}", empleadoIdentificacion.getIdentificacion());

        return ResponseEntity.ok(empleadoIdentificacion);
    }

    @GetMapping("/nombres")
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerEmpleadoPorNombres(@RequestParam("nombres") String nombres) {
        log.info("🔍 [SOLICITUD] Buscar empleado por nombre: {}", nombres);

        EmpleadoRequestDTO empleadoRequestDTO = new EmpleadoRequestDTO();
        empleadoRequestDTO.setNombres(nombres);

        List<EmpleadoResponseDTO> empleadosNombres = empleadoService.obtenerEmpleadoPorNombres(empleadoRequestDTO);

        log.info("📤 [RESPUESTA] Se retornan {} empleados con nombre: {}", empleadosNombres.size(), empleadoRequestDTO.getNombres());

        return ResponseEntity.ok(empleadosNombres);
    }

    @GetMapping("/apellidos")
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerEmpleadoPorApellidos(@RequestParam("apellidos") String apellidos) {
        log.info("🔍 [SOLICITUD] Buscar empleado por apellido: {}", apellidos);

        EmpleadoRequestDTO empleadoRequestDTO = new EmpleadoRequestDTO();
        empleadoRequestDTO.setApellidos(apellidos);

        List<EmpleadoResponseDTO> empleadosApellidos = empleadoService.obtenerEmpleadoPorApellidos(empleadoRequestDTO);

        log.info("📤 [RESPUESTA] Se retornan {} empleados con apellido: {}", empleadosApellidos.size(), empleadoRequestDTO.getApellidos());

        return ResponseEntity.ok(empleadosApellidos);
    }

    @PutMapping("/update")
    public ResponseEntity<EmpleadoResponseDTO> actualizarEmpleado(@RequestBody EmpleadoRequestDTO empleadoRequestDTO) {
        log.info("📥 [SOLICITUD] Actualizar empleado con identificación: {}", empleadoRequestDTO.getIdentificacion());

        EmpleadoResponseDTO empleadoResponseDTO = empleadoService.actualizarEmpleado(empleadoRequestDTO);

        log.info("📤 [RESPUESTA] Empleado actualizado correctamente: {} con identificación: {}", empleadoResponseDTO.getNombres(), empleadoResponseDTO.getIdentificacion());

        return ResponseEntity.ok(empleadoResponseDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> eliminarEmpleado(@RequestParam("identificacion") Long identificacion) {
        log.info("📥 [SOLICITUD] Eliminar empleado con identificación: {}", identificacion);

        EmpleadoRequestDTO empleadoRequestDTO = new EmpleadoRequestDTO();
        empleadoRequestDTO.setIdentificacion(identificacion);

        empleadoService.eliminarEmpleado(empleadoRequestDTO);

        log.info("📤 [RESPUESTA] Empleado eliminado correctamente con identificación: {}", identificacion);

        return ResponseEntity.ok().build();
    }
}
