package co.ventanilla_gimli.repositorios;

import co.ventanilla_gimli.model.DetalleVentaCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleVentaClienteRepo extends JpaRepository<DetalleVentaCliente, Integer> {

    List<DetalleVentaCliente> findAllByCorreoCliente(String correoCliente);
}
