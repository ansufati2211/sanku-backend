package com.sanku.sankuapibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

// Mapea la tabla 'resena' (reseña)
@Entity
@Table(name = "resena")
@Getter @Setter @NoArgsConstructor
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idresena; // Clave primaria

    // Relación Muchos-a-Uno con Paciente (Quién hizo la reseña)
    @ManyToOne
    @JoinColumn(name = "idpaciente", nullable = false)
    private Paciente paciente;

    // Relación Muchos-a-Uno con Psicologo (A quién se reseñó)
    @ManyToOne
    @JoinColumn(name = "idpsicologo", nullable = false)
    private Psicologo psicologo;

    @Column(nullable = false) // Calificación (ej: 1 a 5 estrellas)
    private int calificacion;

    @Column(columnDefinition = "TEXT")
    private String comentario; // Comentario opcional

    private LocalDateTime fecharesena = LocalDateTime.now(); // Fecha automática

    private String estadomoderacion = "Pendiente"; // Estado inicial (Pendiente, Aprobada, Rechazada)
}