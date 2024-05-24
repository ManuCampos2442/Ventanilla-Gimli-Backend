package co.ventanilla_gimli.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class DetalleVentaCliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;
    @Column(nullable = false)
    private String nombreProducto;
    @Column(nullable = false)
    private String descripcion;
    @Column(nullable = false)
    private double precio;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false)
    private LocalDate fechaVenta;
    @Column(nullable = false)
    private String horaDeVenta;
    @Column(nullable = false)
    private String direccion;
    @Column(nullable = false)
    private double devueltas;
    @Column(nullable = false, length = 200)
    private String nombreCliente;
    @Column(nullable = false, length = 200)
    private String correoCliente;
    @Column(nullable = false, length = 20)
    private String telefono;
    @OneToOne
    private VentaCliente venta;

}
