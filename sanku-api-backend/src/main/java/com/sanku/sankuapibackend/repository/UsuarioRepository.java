// Archivo: UsuarioRepository.java
package com.sanku.sankuapibackend.repository;
import com.sanku.sankuapibackend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Boolean existsByEmail(String email);
}