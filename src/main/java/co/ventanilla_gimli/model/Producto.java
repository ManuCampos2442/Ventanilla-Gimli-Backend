package co.ventanilla_gimli.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;
    //@Column(nullable = false)
    //private String nombre;
    @Column(nullable = false)
    private String descripcion;
    @Column(nullable = false)
    private double precio;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false)
    private String proveedor;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Subcategoria subcategoria;


    //@OneToMany(mappedBy = "producto")
    //List<String> nombreAlcoholList = new ArrayList<>();

    @ElementCollection
    private List<String> nombresAlcohol = new ArrayList<>();
    @ElementCollection
    private List<String> nombresDulces = new ArrayList<>();

    @ElementCollection
    private List<String> nombresGaseosas = new ArrayList<>();

    @OneToMany(mappedBy = "producto")
    private List<RegistroProducto> registroProductos;

    @OneToMany(mappedBy = "producto")
    private List<VentaEmpleado> venta;




}
