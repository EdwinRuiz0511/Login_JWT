package com.loginJWT.backend.controller;

import com.loginJWT.backend.dto.UsuarioDTO;
import com.loginJWT.backend.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class controller {

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/crear")
    public ResponseEntity<?> agregarUsuario(@RequestBody UsuarioDTO usuarioDto) {
        try {

            UsuarioDTO usuarioCreado = usuarioService.agregarUsuario(usuarioDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/listarUsuarios")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuariosDto = usuarioService.listarUsuarios();

        return ResponseEntity.ok(usuariosDto);
    }

    @PutMapping("/actualizarUsuario/{id_Usuario}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id_Usuario, @RequestBody UsuarioDTO usuarioDto) {
        try {
            UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id_Usuario, usuarioDto);
            return ResponseEntity.ok(usuarioActualizado);

        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: "+e.getMessage());
        }
    }

    @DeleteMapping("/eliminarUsuario/{id_Usuario}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id_Usuario) {
        try {
            usuarioService.eliminarUsuario(id_Usuario);
            return ResponseEntity.noContent().build();

        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: "+e.getMessage());
        }
    }
}
