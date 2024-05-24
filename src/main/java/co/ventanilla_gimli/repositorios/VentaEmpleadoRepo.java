package co.ventanilla_gimli.repositorios;

import co.ventanilla_gimli.model.Empleado;
import co.ventanilla_gimli.model.VentaEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


public interface VentaEmpleadoRepo extends JpaRepository<VentaEmpleado, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE VentaEmpleado v SET v.empleado = NULL WHERE v.empleado = ?1")
    void desvincularVentasDelEmpleado(Empleado empleado);

    @Modifying
    @Transactional
    @Query("UPDATE RegistroProducto r SET r.empleado = NULL WHERE r.empleado = ?1")
    void desvincularRegistrosDeEmpleado(Empleado empleado);

}
