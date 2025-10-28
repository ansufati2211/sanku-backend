// Archivo: CitaRepository.java
package com.sanku.sankuapibackend.repository;
import com.sanku.sankuapibackend.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {}