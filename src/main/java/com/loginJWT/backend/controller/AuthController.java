package com.loginJWT.backend.controller;

import com.loginJWT.backend.dto.LoginRequestDTO;
import com.loginJWT.backend.dto.UsuarioDTO;
import com.loginJWT.backend.security.serviceSecurity.JwtService;
import com.loginJWT.backend.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/crear")
    public ResponseEntity<?> agregarUsuario(@RequestBody UsuarioDTO usuarioDto) {
        try {

            UsuarioDTO usuarioCreado = usuarioService.agregarUsuario(usuarioDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getUsername(),
                    loginRequestDto.getPassword()
            ));

            // 🔐 Generar el token JWT
            String token = jwtService.generarToken(loginRequestDto.getUsername());

            return  ResponseEntity.ok("✅ Login exictoso, Usuario autenticado ----->  " + token);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: "+e.getMessage());
        }
    }
}
