package com.sanku.sankuapibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Mapea la tabla 'factura'
@Entity
@Table(name = "factura")
@Getter @Setter @NoArgsConstructor
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idfactura; // Clave primaria

    // Relación Muchos-a-Uno con Paciente
    @ManyToOne
    @JoinColumn(name = "idpaciente", nullable = false)
    private Paciente paciente;

    @Column(nullable = false)
    private LocalDate fechaemision;

    private LocalDate fechavencimiento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montototal;

    @Column(nullable = false, length = 50)
    private String estado; // Ej: Pendiente, Pagada, Vencida

    // Relación Uno-a-Muchos con DetalleFactura
    // CascadeType.ALL para que los detalles se guarden/borren con la factura
    // mappedBy indica que la relación la gestiona el campo 'factura' en DetalleFactura
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DetalleFactura> detalles = new ArrayList<>();

    // Método útil para añadir detalles y mantener la consistencia bidireccional
    public void addDetalle(DetalleFactura detalle) {
        detalles.add(detalle);
        detalle.setFactura(this);
    }
}