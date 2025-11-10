package com.cloud.jml.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class EmpleadoRequestDTO {

    private Long identificacion;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String direccion;
}
