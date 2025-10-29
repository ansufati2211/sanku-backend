package com.sanku.sankuapibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

// Mapea la tabla 'paciente'
@Entity
@Table(name = "paciente")
@Getter @Setter @NoArgsConstructor
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idpaciente; // Clave primaria

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false)
    private LocalDate fechanacimiento;

    @Column(length = 50)
    private String numerodocumento;

    @Column(length = 20)
    private String genero;

    @Column(length = 150)
    private String direccion;

    @Column(length = 15)
    private String telefono;

    // Relación Uno-a-Uno con Usuario (Cada paciente tiene una cuenta de usuario)
    // CascadeType.ALL significa que si borras un paciente, también se borra su usuario asociado
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idusuario", nullable = false, unique = true)
    private Usuario usuario;

    // Relación Muchos-a-Uno con TipoDocumento
    @ManyToOne
    @JoinColumn(name = "idtipodocumento", nullable = false)
    private TipoDocumento tipoDocumento;
}