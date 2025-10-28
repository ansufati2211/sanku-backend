package com.sanku.sankuapibackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

// Mapea la tabla 'detallefactura'
@Entity
@Table(name = "detallefactura")
@Getter @Setter @NoArgsConstructor
public class DetalleFactura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer iddetallefactura; // Clave primaria

    // Relación Muchos-a-Uno con Factura (Cada detalle pertenece a una factura)
    @ManyToOne(fetch = FetchType.LAZY) // LAZY para no cargar la factura completa siempre
    @JoinColumn(name = "idfactura", nullable = false)
    @JsonIgnore // Evita bucles infinitos al convertir a JSON
    private Factura factura;

    // Relación Muchos-a-Uno con Servicio (Opcional)
    @ManyToOne
    @JoinColumn(name = "idservicio")
    private Servicio servicio;

    // Relación Muchos-a-Uno con Cita (Opcional)
    @ManyToOne
    @JoinColumn(name = "idcita")
    private Cita cita;

    @Column(nullable = false, length = 255)
    private String descripcion; // Descripción del ítem facturado

    private int cantidad = 1; // Cantidad (por defecto 1)

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal preciounitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal; // cantidad * preciounitario
}