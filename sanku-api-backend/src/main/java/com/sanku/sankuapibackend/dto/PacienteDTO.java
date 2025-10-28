package com.sanku.sankuapibackend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PacienteDTO {
    // --- CORRECTO: El ID ya estaba como Integer, lo cual es consistente ---
    private Integer id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String numeroDocumento;
    private String genero;
}