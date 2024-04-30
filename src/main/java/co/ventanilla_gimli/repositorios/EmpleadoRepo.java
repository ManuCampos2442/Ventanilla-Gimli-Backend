package co.ventanilla_gimli.repositorios;

import co.ventanilla_gimli.model.Empleado;
import co.ventanilla_gimli.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpleadoRepo extends JpaRepository<Empleado, Integer> {

    //Empleado findEmpleadoById(int codigo);
      Empleado findByCorreo(String correo);
    Empleado findByCedula(String cedula);
    List<Empleado> findAll();

}
