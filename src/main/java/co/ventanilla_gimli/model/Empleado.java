package co.ventanilla_gimli.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Empleado extends Usuario implements Serializable {

    @Column(nullable = false)
    private String cedula;
    @OneToMany(mappedBy = "empleado")
    private List<VentaEmpleado> ventas;
    @OneToMany(mappedBy = "empleado")
    private List<RegistroProducto> registroProductos;
}
