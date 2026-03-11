package com.loginJWT.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolDTO {

    private Long id_Rol;
    private String nombreRol; // ADMIN, USER
}
