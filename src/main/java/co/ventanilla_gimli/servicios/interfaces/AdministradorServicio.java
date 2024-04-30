package co.ventanilla_gimli.servicios.interfaces;

import co.ventanilla_gimli.dto.AdministradorDTO.ModificarEmpleadoAdminDTO;
import co.ventanilla_gimli.dto.RegistroEmpleadoDTO;
import co.ventanilla_gimli.model.Empleado;

import java.util.List;

public interface AdministradorServicio {

    List<String> obtenerEmpleados();
    int registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO)throws Exception;
    String modificarEmpleado(ModificarEmpleadoAdminDTO empleadoDTO) throws Exception;
    boolean eliminarCuentaEmpleado(String cedula)throws Exception;

}
