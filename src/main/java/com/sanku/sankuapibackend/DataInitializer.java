package com.sanku.sankuapibackend;

import com.sanku.sankuapibackend.model.ERol;
import com.sanku.sankuapibackend.model.Rol;
import com.sanku.sankuapibackend.model.Usuario;
import com.sanku.sankuapibackend.repository.RolRepository;
import com.sanku.sankuapibackend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired RolRepository rolRepository;
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // --- CORRECCIÓN: Lógica para crear roles usando el Enum ERol ---
        if (rolRepository.findByNombrerol(ERol.ROLE_ADMIN).isEmpty()) {
            rolRepository.save(new Rol(ERol.ROLE_ADMIN));
        }
        if (rolRepository.findByNombrerol(ERol.ROLE_PSICOLOGO).isEmpty()) {
            rolRepository.save(new Rol(ERol.ROLE_PSICOLOGO));
        }
        if (rolRepository.findByNombrerol(ERol.ROLE_PACIENTE).isEmpty()) {
            rolRepository.save(new Rol(ERol.ROLE_PACIENTE));
        }

        // --- CORRECCIÓN: Usar existsByEmail para más eficiencia ---
        if (!usuarioRepository.existsByEmail("admin@sanku.com")) {
            Rol adminRole = rolRepository.findByNombrerol(ERol.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Rol de Admin no encontrado."));
            Usuario admin = new Usuario("admin@sanku.com", passwordEncoder.encode("admin"), adminRole);
            usuarioRepository.save(admin);
            System.out.println(">>> Usuario Administrador creado: admin@sanku.com / admin");
        }
    }
}