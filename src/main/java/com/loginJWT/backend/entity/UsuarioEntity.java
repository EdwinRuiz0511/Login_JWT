package com.loginJWT.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_Usuario;

    private String nombre;
    private String apellido;
    private int edad;
    private String telefono;

    // para login
    private String username;
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_Rol")
    private RolEntity rolEnt;
}
