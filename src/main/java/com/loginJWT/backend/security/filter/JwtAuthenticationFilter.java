package com.loginJWT.backend.security.filter;

import com.loginJWT.backend.security.serviceSecurity.CustomUserDetailsService;
import com.loginJWT.backend.security.serviceSecurity.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. IGNORAR ENDPOINTS PUBLICOS: Endpoints bajo "/auth" son públicos, se omite validación de token
        String endpoint = request.getServletPath();
        if (endpoint.startsWith("/auth")) {
            filterChain.doFilter(request,response);
            return;
        }

        // 2. Obtiene el header Authorization y valida que tenga formato "Bearer <token>".
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);                                                               // EXTRAER EL TOKEN (quitamos la palabra "Bearer "): "Bearer " son 7 caracteres (incluyendo el espacio)
        String username = jwtService.extraerUsername(token);                                                            // Obtener username desde el token

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {                       // Si hay usuario y aún no está autenticado
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);                            // Cargar datos del usuario desde BD

            if (jwtService.tokenEsValido(token)) {                                                                      // Validar que el token sea válido
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken                 // Crear objeto de autenticación
                        (userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));                       // Asociar detalles de la petición
                SecurityContextHolder.getContext().setAuthentication(authToken);                                        // Guardar autenticación en el contexto
            }
        }
        filterChain.doFilter(request, response);                                                                        // Continuar con la petición
    }
}
