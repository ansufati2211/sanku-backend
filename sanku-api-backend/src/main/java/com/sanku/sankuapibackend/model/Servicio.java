package com.sanku.sankuapibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

// Mapea la tabla 'servicio'
@Entity
@Table(name = "servicio")
@Getter @Setter @NoArgsConstructor
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idservicio; // Clave primaria

    @Column(nullable = false, length = 150)
    private String nombreservicio; // Ej: Terapia Individual Adultos, Paquete 8 sesiones

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 8, scale = 2) // Para precios como 120.00
    private BigDecimal precio;
}