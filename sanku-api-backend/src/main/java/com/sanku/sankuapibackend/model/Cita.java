package com.sanku.sankuapibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

// Mapea la tabla 'cita'
@Entity
@Table(name = "cita")
@Getter @Setter @NoArgsConstructor
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idcita; // Clave primaria

    @Column(nullable = false)
    private LocalDateTime fechahorainicio;

    @Column(nullable = false)
    private LocalDateTime fechahorafin;

    // Relación Muchos-a-Uno con EstadoCita
    @ManyToOne
    @JoinColumn(name = "idestadocita", nullable = false)
    private EstadoCita estadoCita;

    // Relación Muchos-a-Uno con Paciente
    @ManyToOne
    @JoinColumn(name = "idpaciente", nullable = false)
    private Paciente paciente;

    // Relación Muchos-a-Uno con Psicologo
    @ManyToOne
    @JoinColumn(name = "idpsicologo", nullable = false)
    private Psicologo psicologo;

    // Relación Muchos-a-Uno con Servicio (Opcional, puede ser null)
    @ManyToOne
    @JoinColumn(name = "idservicio")
    private Servicio servicio;
}