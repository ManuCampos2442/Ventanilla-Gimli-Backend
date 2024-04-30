package co.ventanilla_gimli.repositorios;

import co.ventanilla_gimli.model.VentaCliente;
import co.ventanilla_gimli.model.VentaEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface VentaClienteRepo extends JpaRepository<VentaCliente, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE VentaCliente v SET v.cliente = NULL WHERE v.cliente.codigo = ?1")
    void desvincularVentasDelCliente(int codigoCliente);

    @Modifying
    @Transactional
    @Query("UPDATE VentaEmpleado v SET v.cliente = NULL WHERE v.cliente.codigo = ?1")
    void desvincularVentasDelCliente2(int codigoCliente);
}

