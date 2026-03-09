package com.loginJWT.backend.service;

import com.loginJWT.backend.dto.UsuarioDTO;

import java.util.List;

public interface IUsuarioService {

    UsuarioDTO agregarUsuario(UsuarioDTO usuarioDto);

    List<UsuarioDTO> listarUsuarios();

    UsuarioDTO actualizarUsuario(Long id_Usuario, UsuarioDTO usuarioDto);

    void eliminarUsuario(Long id_Usuario);
}
