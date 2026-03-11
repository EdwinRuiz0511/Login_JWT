package com.loginJWT.backend.service.impl;

import com.loginJWT.backend.dto.UsuarioDTO;
import com.loginJWT.backend.entity.RolEntity;
import com.loginJWT.backend.entity.UsuarioEntity;
import com.loginJWT.backend.repository.IRolRepository;
import com.loginJWT.backend.repository.IUsuarioRepository;
import com.loginJWT.backend.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IRolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO agregarUsuario(UsuarioDTO usuarioDto) {

        // Validar si el username ya existe
        if (usuarioRepository.findByUsername(usuarioDto.getUsername()).isPresent()) {
            throw new RuntimeException("Error: el username -> "+usuarioDto.getUsername()+" ya existe");
        }

        // Buscar rol USER
        RolEntity rolEnt = rolRepository.findByNombreRol("USER")
                .orElseThrow(() -> new RuntimeException("Erro: Rol USER no existe"));


        // Crear Usuario: Pasamos de dto --> entity
        UsuarioEntity usuarioEnt = new UsuarioEntity();
        usuarioEnt.setNombre(usuarioDto.getNombre());
        usuarioEnt.setApellido(usuarioDto.getApellido());
        usuarioEnt.setEdad(usuarioDto.getEdad());
        usuarioEnt.setTelefono(usuarioDto.getTelefono());
        usuarioEnt.setUsername(usuarioDto.getUsername());

        // Encriptar password antes de guardar
        usuarioEnt.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));

        // Asignar Rol
        usuarioEnt.setRolEnt(rolEnt);

        // Guardamos en la base de datos
        UsuarioEntity usuarioGuardado = usuarioRepository.save(usuarioEnt);

        // Pasamos de entity --> dto. Para simplemente darle una respuesta a postman de lo que se a creado
        UsuarioDTO respuesta = new UsuarioDTO();
        respuesta.setId_Usuario(usuarioGuardado.getId_Usuario());
        respuesta.setNombre(usuarioGuardado.getNombre());
        respuesta.setApellido(usuarioGuardado.getApellido());
        respuesta.setEdad(usuarioGuardado.getEdad());
        respuesta.setTelefono(usuarioGuardado.getTelefono());

        return respuesta;
    }

    @Override
    public List<UsuarioDTO> listarUsuarios() {
        // Mandamos a traer en una lista todos los usuarios de la base de datos
        List<UsuarioEntity> usuariosEnt = usuarioRepository.findAll();
        // Creamos otra lista donde vamos a pasar de Entity --> dto
        List<UsuarioDTO> usuariosDto = new ArrayList<>();

        for(UsuarioEntity usuarioEnt : usuariosEnt) {
            UsuarioDTO usuarioDto = new UsuarioDTO();
            usuarioDto.setId_Usuario(usuarioEnt.getId_Usuario());
            usuarioDto.setNombre(usuarioEnt.getNombre());
            usuarioDto.setApellido(usuarioEnt.getApellido());
            usuarioDto.setEdad(usuarioEnt.getEdad());
            usuarioDto.setTelefono(usuarioEnt.getTelefono());

            // Agregamos usuarioDto a lista usuariosDto
            usuariosDto.add(usuarioDto);
        }
        return usuariosDto;
    }

    @Override
    public UsuarioDTO actualizarUsuario(Long id_Usuario, UsuarioDTO usuarioDto) {

        // Validamos que el Usuario si este en la base de datos y lo buscamos por su ID
        UsuarioEntity usuarioEnt = usuarioRepository.findById(id_Usuario)
                .orElseThrow(() -> new RuntimeException("Usuario con ID -> "+id_Usuario+" No encontrado!!!"));

        // DTO → ENTITY (actualización)
        usuarioEnt.setNombre(usuarioDto.getNombre());
        usuarioEnt.setApellido(usuarioDto.getApellido());
        usuarioEnt.setEdad(usuarioDto.getEdad());
        usuarioEnt.setTelefono(usuarioDto.getTelefono());

        // ENTITY → BASE DE DATOS
        UsuarioEntity usuarioActualizado = usuarioRepository.save(usuarioEnt);

        // ENTITY → DTO
        UsuarioDTO respuesta = new UsuarioDTO();
        respuesta.setId_Usuario(usuarioActualizado.getId_Usuario());
        respuesta.setNombre(usuarioActualizado.getNombre());
        respuesta.setApellido(usuarioActualizado.getApellido());
        respuesta.setEdad(usuarioActualizado.getEdad());
        respuesta.setTelefono(usuarioActualizado.getTelefono());

        return respuesta;
    }

    @Override
    public void eliminarUsuario(Long id_Usuario) {

        // Validamos que el Usuario si este en la base de datos y lo buscamos por su ID
        UsuarioEntity usuarioEnt = usuarioRepository.findById(id_Usuario)
                .orElseThrow(() -> new RuntimeException("Usuario con ID -> "+id_Usuario+" No encontrado!!!"));

        usuarioRepository.delete(usuarioEnt);
    }
}
