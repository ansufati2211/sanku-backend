package com.sanku.sankuapibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Mapea la tabla 'tipodocumento'
@Entity
@Table(name = "tipodocumento")
@Getter @Setter @NoArgsConstructor
public class TipoDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idtipodocumento; // Clave primaria

    @Column(nullable = false, unique = true, length = 50)
    private String tipodocumento; // Ej: DNI, Pasaporte, Carnet Extranjería
}