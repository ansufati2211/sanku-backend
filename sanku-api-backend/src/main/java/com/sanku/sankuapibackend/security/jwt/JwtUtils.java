package com.sanku.sankuapibackend.security.jwt;

import com.sanku.sankuapibackend.security.services.UserDetailsImpl;
import io.jsonwebtoken.*; // Asegúrate de que este import general esté presente
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException; // Import explícito
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey; // Usar SecretKey explícitamente
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${sanku.app.jwtSecret}")
    private String jwtSecret;

    @Value("${sanku.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Genera un token JWT para el usuario autenticado.
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS256) // Especificar algoritmo aquí
                .compact();
    }

    /**
     * Obtiene la llave secreta para firmar y verificar tokens.
     */
    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extrae el nombre de usuario (email) del token JWT.
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key()) // Configurar la llave de verificación
                .build()              // Construir el parser
                .parseClaimsJws(token)// Parsear y verificar firma/expiración, obteniendo Claims
                .getBody()            // Obtener el cuerpo (Claims)
                .getSubject();        // Obtener el 'subject' (username/email)
    }

    /**
     * Valida la firma y la expiración del token JWT.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key()) // Configurar la llave de verificación
                .build()              // Construir el parser
                .parse(authToken);    // Intentar parsear (verifica firma y expiración)
            return true;
        // Capturar excepciones específicas primero
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}