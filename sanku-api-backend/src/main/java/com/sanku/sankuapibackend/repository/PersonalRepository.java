// Archivo: PersonalRepository.java
package com.sanku.sankuapibackend.repository;
import com.sanku.sankuapibackend.model.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PersonalRepository extends JpaRepository<Personal, Integer> {}