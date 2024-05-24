package co.ventanilla_gimli.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class VentaCliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;

    @Column(nullable = false)
    private LocalDate fechaVenta;
    @Column(nullable = false)
    private String horaDeVenta;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false)
    private double precioUnitario;
    @Column(nullable = false)
    private String direccion;
    @Column(nullable = false)
    private double dinero;
    //______________________________________________________________________________________

    //__________________________________ FK ________________________________________________

    @ManyToOne(cascade = CascadeType.ALL)
    private Cliente cliente;
    @ManyToOne
    private  Producto producto;

    @OneToOne(mappedBy = "venta")
    private DetalleVentaCliente detalleVenta;
}
