package com.sanku.sankuapibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Mapea la tabla 'especialidad'
@Entity
@Table(name = "especialidad")
@Getter @Setter @NoArgsConstructor
public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idespecialidad; // Clave primaria

    @Column(nullable = false, unique = true, length = 100)
    private String nombreespecialidad; // Ej: Terapia Cognitivo-Conductual

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcionespecialidad;
}