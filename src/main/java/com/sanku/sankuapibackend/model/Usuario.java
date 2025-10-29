package com.sanku.sankuapibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Getter @Setter @NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idusuario;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String contrasenia;

    @Column(updatable = false)
    private LocalDateTime fechacreacion = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idrol", nullable = false)
    private Rol rol;

    public Usuario(String email, String contrasenia, Rol rol) {
        this.email = email;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }
}