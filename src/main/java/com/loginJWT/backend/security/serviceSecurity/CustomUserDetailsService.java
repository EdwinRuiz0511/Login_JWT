package com.loginJWT.backend.security.serviceSecurity;

import com.loginJWT.backend.entity.UsuarioEntity;
import com.loginJWT.backend.repository.IUsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j                                                                                                                  // Habilita el uso de logs (debug, info, warn, etc.) mediante Lombok
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    // Carga un usuario desde la base de datos para el proceso de autenticación (login)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Intentando cargar usuario para la autenticacion: {}",username);

        // Busca el usuario en la base de datos por username
        UsuarioEntity usuarioEnt = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> { log.warn("Usuario no encontrado: --> {}", username);
                        return new UsernameNotFoundException("Usuario no encontrado: --> " + username);
                });

        // Log informativo que confirma que el usuario fue encontrado
        log.info("Usuario encontrado: {}", usuarioEnt.getUsername());

        // Construye el objeto UserDetails que Spring Security usará para autenticar al usuario
        return User.builder()
                .username(usuarioEnt.getUsername())
                .password(usuarioEnt.getPassword())
                .roles(usuarioEnt.getRolEnt().getNombreRol())
                .build();
    }
}
