package com.sanku.sankuapibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

// Mapea la tabla 'psicologo'
@Entity
@Table(name = "psicologo")
@Getter @Setter @NoArgsConstructor
public class Psicologo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idpsicologo; // Clave primaria

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    private Integer experienciaanios;

    @Column(nullable = false, unique = true, length = 50)
    private String numerolicencia; // Número de colegiatura

    @Column(length = 255)
    private String fotourl; // URL de la foto de perfil

    @Column(length = 5) // Ej: 'ACT' (Activo), 'INA' (Inactivo)
    private String estado;

    // Relación Uno-a-Uno con Usuario (Cada psicólogo tiene una cuenta)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idusuario", nullable = false, unique = true)
    private Usuario usuario;

    // Relación Muchos-a-Muchos con Especialidad
    // FetchType.LAZY para no cargar todas las especialidades a menos que se necesiten
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "psicologo_especialidad", // Tabla intermedia
            joinColumns = @JoinColumn(name = "idpsicologo"),
            inverseJoinColumns = @JoinColumn(name = "idespecialidad"))
    private Set<Especialidad> especialidades = new HashSet<>(); // Usamos Set para evitar duplicados
}