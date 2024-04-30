package co.ventanilla_gimli.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class RegistroProducto implements Serializable {

    //___________________________________ Atributos y PK ______________________________________________
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;
    @Column(nullable = true)
    private String nombreProducto;
    @Column(nullable = true)
    private int cantidad;
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Subcategoria subcategoria;
    @Column(nullable = true)
    private LocalDate fechaRegistro;
    @Column(nullable = true)
    private String horaDeRegistro;

    //__________________________________ FK ________________________________________________
    @ManyToOne
    private Producto producto;
    @ManyToOne
    private Empleado empleado;

}
