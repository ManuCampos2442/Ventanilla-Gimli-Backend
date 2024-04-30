package co.ventanilla_gimli.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@MappedSuperclass // Borrar por si las moscas
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Usuario extends Cuenta implements Serializable {

    @Column(nullable = false, length = 200)
    private String nombre;
    @Column(nullable = false, length = 20)
    private String telefono;
    @Column(nullable = false)
    private boolean estado;

}
