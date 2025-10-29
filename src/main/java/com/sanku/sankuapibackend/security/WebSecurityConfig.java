package com.sanku.sankuapibackend.security;

import com.sanku.sankuapibackend.security.jwt.AuthEntryPointJwt;
import com.sanku.sankuapibackend.security.jwt.AuthTokenFilter;
import com.sanku.sankuapibackend.security.services.UserDetailsServiceImpl;

// Imports añadidos para CORS
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import static org.springframework.security.config.Customizer.withDefaults;
// Import necesario para HttpMethod
import org.springframework.http.HttpMethod;
// Fin de imports para CORS

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    // Ya no hay @Autowired para PasswordEncoder aquí

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        // Llama al método directamente
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Habilita CORS usando el Bean 'corsConfigurationSource'
            .cors(withDefaults())

            // Deshabilita CSRF
            .csrf(csrf -> csrf.disable())

            // Manejo de excepciones JWT
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))

            // Sesiones STATELESS
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Reglas de autorización
            .authorizeHttpRequests(auth -> auth
                // --- ¡ESTA ES LA LÍNEA QUE FALTABA! ---
                // Permite TODAS las peticiones OPTIONS (preflight CORS) sin autenticación.
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // --- FIN DE LA LÍNEA QUE FALTABA ---

                // Rutas públicas
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/test/**").permitAll()

                // Todas las demás requieren autenticación
                .anyRequest().authenticated()
            );

        // Configura el proveedor de autenticación
        http.authenticationProvider(authenticationProvider());

        // Añade el filtro JWT
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Para prototipo con texto plano
        return NoOpPasswordEncoder.getInstance();
    }

    // Bean de CORS (con los orígenes de tu error incluidos)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5500",
            "http://127.0.0.1:5500",
            "http://localhost:5502",  // Puerto del error
            "http://127.0.0.1:5502", // Origen exacto del error
            "http://localhost:3000"   // Puerto React/Vue
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a todas las rutas

        return source;
    }
}