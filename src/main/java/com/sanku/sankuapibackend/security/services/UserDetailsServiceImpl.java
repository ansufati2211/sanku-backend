package com.sanku.sankuapibackend.security.services;

import com.sanku.sankuapibackend.model.Usuario;
import com.sanku.sankuapibackend.repository.UsuarioRepository; // Asegúrate que el nombre del paquete sea correcto
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Asegúrate de que el nombre de la variable coincida con el nombre de tu clase Repository
    // Por ejemplo, si tu clase es UsuarioRepositorio, cambia el nombre aquí
    @Autowired
    UsuarioRepository usuarioRepository; // Usualmente empieza con minúscula por convención

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca al usuario por email usando el repositorio
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // --- LÍNEAS DE DEPURACIÓN AÑADIDAS ---
        // Imprime el email encontrado en la consola del backend
        System.out.println(">>> Usuario encontrado por UserDetailsService: " + user.getEmail());
        // Imprime la contraseña EXACTA recuperada de la entidad Java (antes de la comparación)
        System.out.println(">>> Contraseña recuperada de la Entidad BD: '" + user.getContrasenia() + "'");
        // --- FIN LÍNEAS DE DEPURACIÓN ---

        // Construye y devuelve el objeto UserDetails que Spring Security necesita
        return UserDetailsImpl.build(user);
    }
}