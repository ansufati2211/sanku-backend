package com.sanku.sankuapibackend.repository;

import com.sanku.sankuapibackend.model.ERol;
import com.sanku.sankuapibackend.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    // --- CORRECCIÓN: El método ahora busca por el tipo Enum ERol, no por String ---
    Optional<Rol> findByNombrerol(ERol nombreRol);
}