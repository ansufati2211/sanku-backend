package com.sanku.sankuapibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// Mapea la tabla 'pago'
@Entity
@Table(name = "pago")
@Getter @Setter @NoArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idpago; // Clave primaria

    // Relación Muchos-a-Uno con Factura (Un pago puede cubrir parte de una factura)
    @ManyToOne
    @JoinColumn(name = "idfactura", nullable = false)
    private Factura factura;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto; // Monto pagado

    @Column(nullable = false)
    private LocalDateTime fechapago; // Fecha y hora exactas del pago

    @Column(length = 50)
    private String metodopago; // Ej: Efectivo, Tarjeta, Yape, Transferencia
}