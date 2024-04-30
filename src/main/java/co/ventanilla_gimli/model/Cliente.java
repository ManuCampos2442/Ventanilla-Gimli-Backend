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
public class Cliente extends Usuario implements Serializable {

    @Column(nullable = false)
    private String direccion;

    @OneToMany(mappedBy = "cliente")
    List<VentaCliente> ventas;

}
