package com.sanku.sankuapibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

// Mapea la tabla 'personal' (Sugerida para RRHH)
@Entity
@Table(name = "personal")
@Getter @Setter @NoArgsConstructor
public class Personal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idpersonal; // Clave primaria

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, length = 50)
    private String rol; // Ej: 'Administrador', 'Asistente', 'Recepcionista'

    private LocalDate fechaingreso;

    @Column(precision = 10, scale = 2)
    private BigDecimal salario;

    // Relación Uno-a-Uno con Usuario (Cada miembro del personal tiene una cuenta)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idusuario", nullable = false, unique = true)
    private Usuario usuario;
}