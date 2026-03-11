package com.loginJWT.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    // Para login
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Se permite recibir el username del usuario en el request (POST), pero no se expone en las respuestas JSON (response).
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Se permite recibir password id del usuario en el request (POST), pero no se expone en las respuestas JSON (response).
    private String password;
}
