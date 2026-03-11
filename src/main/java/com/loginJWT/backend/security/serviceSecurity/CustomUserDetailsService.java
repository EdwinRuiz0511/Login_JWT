package com.loginJWT.backend.security.serviceSecurity;

import com.loginJWT.backend.entity.UsuarioEntity;
import com.loginJWT.backend.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    // Carga un usuario desde la base de datos para el proceso de autenticación (login)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UsuarioEntity usuarioEnt = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: --> " + username));

        return User.builder()
                .username(usuarioEnt.getUsername())
                .password(usuarioEnt.getPassword())
                .roles(usuarioEnt.getRolEnt().getNombreRol())
                .build();
    }
}
