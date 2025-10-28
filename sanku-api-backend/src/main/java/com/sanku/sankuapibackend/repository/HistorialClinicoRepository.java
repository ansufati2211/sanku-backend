package com.sanku.sankuapibackend.repository;

import com.sanku.sankuapibackend.model.HistorialClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico, Integer> {
}