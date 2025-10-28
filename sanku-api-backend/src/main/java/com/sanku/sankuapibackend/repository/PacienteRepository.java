// Archivo: PacienteRepository.java
package com.sanku.sankuapibackend.repository;
import com.sanku.sankuapibackend.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {}