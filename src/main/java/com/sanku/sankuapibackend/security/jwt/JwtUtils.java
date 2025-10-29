package com.sanku.sankuapibackend.security.jwt;

import com.sanku.sankuapibackend.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException; // Import explícito
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Lee la clave secreta desde application.properties
    @Value("${sanku.app.jwtSecret}")
    private String jwtSecret;

    // Lee el tiempo de expiración desde application.properties
    @Value("${sanku.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Genera un token JWT para el usuario autenticado.
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        System.out.println(">>> JwtUtils generating token for: " + userPrincipal.getUsername()); // Log

        // Asegúrate que jwtSecret no sea null o vacío aquí
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            System.err.println("!!! ERROR: jwtSecret is missing or empty in application.properties!");
            // Considera lanzar una excepción aquí en un entorno real
        } else {
             System.out.println(">>> JwtUtils using secret (first 5 chars): " + jwtSecret.substring(0, Math.min(jwtSecret.length(), 5)) + "..."); // Log (muestra solo el inicio)
        }


        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // Guarda el email en el 'subject'
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS256) // Firma con HS256
                .compact();
    }

    /**
     * Obtiene la SecretKey a partir de la cadena jwtSecret (decodificada de Base64).
     */
    private SecretKey key() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrae el nombre de usuario (email) del token JWT.
     */
    public String getUserNameFromJwtToken(String token) {
         System.out.println(">>> JwtUtils extracting username from token..."); // Log
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Valida la firma y la expiración del token JWT.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            // --- LOG DE DEPURACIÓN ---
            System.out.println(">>> JwtUtils attempting to validate token: " + (authToken != null && authToken.length() > 10 ? authToken.substring(0, 10) + "..." : authToken));
            // --- FIN LOG ---

            Jwts.parserBuilder()
                .setSigningKey(key()) // Usa la misma clave para verificar
                .build()
                .parse(authToken);    // Intenta parsear (verifica firma y expiración automáticamente)

            // --- LOG DE DEPURACIÓN ---
            System.out.println(">>> JwtUtils validation SUCCESSFUL.");
            // --- FIN LOG ---
            return true;
        // Captura excepciones específicas primero para dar logs más claros
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            // --- LOG DE DEPURACIÓN ---
            System.err.println("!!! JWT Validation Error: Invalid Signature - " + e.getMessage());
            // --- FIN LOG ---
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
             // --- LOG DE DEPURACIÓN ---
            System.err.println("!!! JWT Validation Error: Malformed Token - " + e.getMessage());
             // --- FIN LOG ---
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
             // --- LOG DE DEPURACIÓN ---
            System.err.println("!!! JWT Validation Error: Token Expired - " + e.getMessage());
             // --- FIN LOG ---
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
             // --- LOG DE DEPURACIÓN ---
            System.err.println("!!! JWT Validation Error: Unsupported Token - " + e.getMessage());
             // --- FIN LOG ---
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
             // --- LOG DE DEPURACIÓN ---
            System.err.println("!!! JWT Validation Error: Illegal Argument (often null/empty token) - " + e.getMessage());
             // --- FIN LOG ---
        } catch (Exception e) { // Captura genérica por si acaso
             logger.error("Unexpected JWT validation error: {}", e.getMessage());
             // --- LOG DE DEPURACIÓN ---
             System.err.println("!!! JWT Validation Error: Unexpected (" + e.getClass().getSimpleName() + ") - " + e.getMessage());
             // --- FIN LOG ---
        }

        // --- LOG DE DEPURACIÓN ---
        System.err.println("!!! JwtUtils validation FAILED.");
        // --- FIN LOG ---
        return false;
    }
}