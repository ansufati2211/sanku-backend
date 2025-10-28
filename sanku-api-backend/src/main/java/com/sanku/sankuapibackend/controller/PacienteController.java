package com.sanku.sankuapibackend.controller;

import com.sanku.sankuapibackend.dto.PacienteDTO;
import com.sanku.sankuapibackend.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/pacientes")
public class PacienteController {

   @Autowired
    private PacienteService pacienteService;

  @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PSICOLOGO')")
    public ResponseEntity<List<PacienteDTO>> getAllPacientes() {
        return ResponseEntity.ok(pacienteService.getAllPacientes());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PSICOLOGO')")
    public ResponseEntity<PacienteDTO> getPacienteById(@PathVariable Integer id) {
        return pacienteService.getPacienteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // --- FIN DE LA CORRECCIÓN ---

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PacienteDTO> createPaciente(@RequestBody PacienteDTO pacienteDTO) {
        // La lógica de creación completa requerirá manejar el usuario y tipo de documento
        return new ResponseEntity<>(pacienteService.createPaciente(pacienteDTO), HttpStatus.CREATED);
    }

    // --- INICIO DE LA CORRECCIÓN ---
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PacienteDTO> updatePaciente(@PathVariable Integer id, @RequestBody PacienteDTO pacienteDTO) { // Cambiado de Long a Integer
        return pacienteService.updatePaciente(id, pacienteDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // --- FIN DE LA CORRECCIÓN ---

    // --- INICIO DE LA CORRECCIÓN ---
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePaciente(@PathVariable Integer id) { // Cambiado de Long a Integer
        return pacienteService.deletePaciente(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
    // --- FIN DE LA CORRECCIÓN ---
}