package com.sanku.sankuapibackend.repository;

import com.sanku.sankuapibackend.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoCitaRepository extends JpaRepository<EstadoCita, Integer> {
}