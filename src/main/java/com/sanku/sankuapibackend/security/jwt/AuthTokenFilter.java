package com.sanku.sankuapibackend.security.jwt;

import com.sanku.sankuapibackend.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter; // Asegúrate que sea este import

// Si NO creas este filtro como un @Bean en WebSecurityConfig, necesitas @Component
// import org.springframework.stereotype.Component;
// @Component // Descomenta si AuthTokenFilter NO es un @Bean

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // --- LOGS DE DEPURACIÓN ---
        String requestURI = request.getRequestURI();
        System.out.println("\n>>> AuthTokenFilter processing request: " + request.getMethod() + " " + requestURI);

        // No procesar el filtro para rutas de autenticación públicas
        // Ajusta el path si es necesario (ej. si tu login está en /api/v1/auth)
        if (requestURI.startsWith("/api/auth/")) {
             System.out.println(">>> Skipping AuthTokenFilter for auth path.");
             filterChain.doFilter(request, response);
             return;
        }
        // --- FIN LOGS ---

        try {
            String jwt = parseJwt(request);

            // --- LOGS DE DEPURACIÓN ---
            // Imprime los primeros 10 caracteres del token o "null"
            System.out.println(">>> JWT Token parsed: " + (jwt != null && jwt.length() > 10 ? jwt.substring(0, 10) + "..." : jwt));
            // --- FIN LOGS ---

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                 // --- LOGS DE DEPURACIÓN ---
                System.out.println(">>> JWT Token validation successful.");
                 // --- FIN LOGS ---

                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                 // --- LOGS DE DEPURACIÓN ---
                System.out.println(">>> Username from token: " + username);
                 // --- FIN LOGS ---

                // Carga los detalles del usuario desde la base de datos
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Crea el objeto de autenticación para Spring Security
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establece la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);

                 // --- LOGS DE DEPURACIÓN ---
                System.out.println(">>> SecurityContext updated for user: " + username);
                 // --- FIN LOGS ---
            } else {
                 // --- LOGS DE DEPURACIÓN ---
                // Esto se imprimirá si el token es null o si jwtUtils.validateJwtToken devuelve false
                System.out.println(">>> JWT Token is null or validation failed by jwtUtils.");
                 // --- FIN LOGS ---
            }
        } catch (Exception e) {
            // Loguea cualquier error inesperado durante el proceso
            logger.error("Cannot set user authentication: {}", e.getMessage());
             // --- LOGS DE DEPURACIÓN ---
            System.err.println("!!! Exception in AuthTokenFilter's try-catch block: " + e.getMessage());
             // --- FIN LOGS ---
        }

        // Continúa con la cadena de filtros (importante para que la petición siga)
        filterChain.doFilter(request, response);
    }

    // Método para extraer el token de la cabecera Authorization
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        // --- LOGS DE DEPURACIÓN ---
        System.out.println(">>> Authorization Header received: " + headerAuth);
        // --- FIN LOGS ---

        // Verifica si la cabecera existe, tiene texto y empieza con "Bearer "
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            // Extrae el token (todo después de "Bearer ")
            return headerAuth.substring(7);
        }

        // Devuelve null si no se encuentra un token válido en la cabecera
        return null;
    }
}