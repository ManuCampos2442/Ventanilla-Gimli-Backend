package co.ventanilla_gimli.repositorios;

import co.ventanilla_gimli.model.RegistroProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroProductosRepo extends JpaRepository<RegistroProducto, Integer> {

    List<RegistroProducto> findAll();

}
