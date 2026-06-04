package com.loginJWT.backend.security.serviceSecurity;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    // 🔐 Generar token
    public String generarToken(String username) {
        return Jwts.builder()
                .subject(username)                                                                                      // Aqui guardamos el username del usuario autenticado
                .issuedAt(new Date())                                                                                   // Fecha en la que el token fue creado
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration))) // 30min             // Fecha de expiracion
                .signWith(obtenerClaveSecreta(), SignatureAlgorithm.HS256)                                              // Firma el token con tu clave secreta
                .compact();                                                                                             // Empaqueta todo en un sprinf tipo: eyJhbGciOiJIUzI1NiJ9...
    }

    // Obtener firma del token
    public Key obtenerClaveSecreta() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);                                                            // Convierte el string Base64 a bytes
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Método que recibe un JWT y devuelve sus datos internos (claims)
    private Claims extraerClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) obtenerClaveSecreta())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("JWT expirado en extraerClaims: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.error("JWT mal formado en extraerClaims: {}", e.getMessage());
            throw e;
        } catch (SignatureException e) {
            log.error("Firma inválida en extraerClaims: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("Token vacío o null en extraerClaims: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado en extraerClaims: {}", e.getMessage());
            throw e;
        }
    }

    // Este método recibe un token JWT y devuelve el username del usuario que está dentro del token.
    public String extraerUsername(String token) {
        return extraerClaims(token).getSubject();
    }

    // Método que valida si un JWT es válido.
    public boolean tokenEsValido(String token) {
        try {
            Claims claims = extraerClaims(token);                                                                       // Obtén los datos del token
            return !claims.getExpiration().before(new Date());                                                          // Verifica que el token no esté expirado, ¿La expiración NO es antes de ahora?

        } catch (ExpiredJwtException e) {                                                                               // El token ya expiro
            log.warn("JWT expirado: {}", e.getMessage());

        } catch (MalformedJwtException e) {                                                                             // El token esta mal formado o manipulado
            log.error("JWT mal formado: {}", e.getMessage());

        } catch (SignatureException e) {                                                                                // Firma invalida o posible Falsificacion
            log.error("Firma invalida: {}", e.getMessage());

        } catch (IllegalArgumentException e) {                                                                          // Token vacio o null
            log.error("Token vacio o null: {}", e.getMessage());

        } catch (Exception e) {                                                                                         // Cualquier otro error inesperado
            log.error("Error inesperado validando JWT: {}", e);
        }
        return false;
    }
}
