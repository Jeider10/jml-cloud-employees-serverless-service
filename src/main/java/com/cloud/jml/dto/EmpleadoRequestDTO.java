package com.cloud.jml.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class EmpleadoRequestDTO {

    // Agregado: validaciones de Bean Validation para garantizar integridad de datos en los endpoints
    @NotNull(message = "La identificacion es obligatoria")
    private Long identificacion;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellidos;

    @Size(max = 20, message = "El telefono no puede exceder 20 caracteres")
    private String telefono;

    @Size(max = 255, message = "La direccion no puede exceder 255 caracteres")
    private String direccion;
}
