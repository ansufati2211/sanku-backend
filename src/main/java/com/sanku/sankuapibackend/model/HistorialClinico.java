package com.sanku.sankuapibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

// Mapea la tabla 'historialclinico'
@Entity
@Table(name = "historialclinico")
@Getter @Setter @NoArgsConstructor
public class HistorialClinico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idhistorial; // Clave primaria

    // Relación Uno-a-Uno con Cita (Cada historial pertenece a una única cita)
    @OneToOne
    @JoinColumn(name = "idcita", nullable = false, unique = true)
    private Cita cita;

    @Column(columnDefinition = "TEXT")
    private String notassesion; // Notas que toma el psicólogo

    @Column(columnDefinition = "TEXT")
    private String progresoobservado;

    @Column(columnDefinition = "TEXT")
    private String plantratamiento;

    @Column(updatable = false)
    private LocalDateTime fechacreacion = LocalDateTime.now(); // Fecha de creación automática
}