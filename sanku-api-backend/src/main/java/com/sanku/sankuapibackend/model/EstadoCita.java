package com.sanku.sankuapibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Mapea la tabla 'estadocita'
@Entity
@Table(name = "estadocita")
@Getter @Setter @NoArgsConstructor
public class EstadoCita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idestadocita; // Clave primaria

    @Column(length = 150)
    private String estadocita; // Ej: Programada, Confirmada, Cancelada, Realizada
}