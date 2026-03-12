package com.loginJWT.backend.security.serviceSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
public class JwtService {

    private static final String SECRET_KEY = "mi_clave_secreta_super_segura_para_jwt_123456";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // 🔐 Generar token
    public String generarToken(String username) {
        return Jwts.builder()
                .subject(username)                                                                                      // Aqui guardamos el username del usuario autenticado
                .issuedAt(new Date())                                                                                   // Fecha en la que el token fue creado
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30min                             // Fecha de expiracion
                .signWith(getSigningKey())                                                                              // Firma el token con tu clave secreta
                .compact();                                                                                             // Empaqueta todo en un sprinf tipo: eyJhbGciOiJIUzI1NiJ9...
    }

    // Método que recibe un JWT y devuelve sus datos internos (claims)
    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())                                                                            // Verifica la firma
                .build()                                                                                                // Esto simplemente finaliza la configuración del parser.
                .parseSignedClaims(token)                                                                               // Decodifica el JWT, Verifica la firma, Verifica estructura
                .getPayload();                                                                                          // Aquí se extrae el contenido del token, es decir los claims.
    }

    // Este método recibe un token JWT y devuelve el username del usuario que está dentro del token.
    public String extraerUsername(String token) {
        return extraerClaims(token).getSubject();
    }

    // Método que valida si un JWT es válido.
    public boolean tokenEsValido(String token) {
        try {
            Claims claims = extraerClaims(token);
            return !claims.getExpiration().before(new Date());                                                          // Verifica que el token no esté expirado

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
