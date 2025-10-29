// Archivo: PsicologoRepository.java
package com.sanku.sankuapibackend.repository;
import com.sanku.sankuapibackend.model.Psicologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PsicologoRepository extends JpaRepository<Psicologo, Integer> {}