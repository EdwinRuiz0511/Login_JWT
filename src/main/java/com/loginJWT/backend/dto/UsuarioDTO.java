package com.loginJWT.backend.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
        "id_Usuario",
        "nombre",
        "apellido",
        "edad",
        "telefono"
})
public class UsuarioDTO {

    private Long id_Usuario;
    private String nombre;
    private String apellido;
    private int edad;
    private String telefono;

}
