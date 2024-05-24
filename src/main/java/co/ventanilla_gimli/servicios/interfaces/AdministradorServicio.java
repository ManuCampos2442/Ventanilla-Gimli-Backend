package co.ventanilla_gimli.servicios.interfaces;

import co.ventanilla_gimli.dto.*;
import co.ventanilla_gimli.dto.AdministradorDTO.ItemEmpleadoDTO;
import co.ventanilla_gimli.dto.AdministradorDTO.ModificarEmpleadoAdminDTO;
import co.ventanilla_gimli.dto.ClienteDTO.DetalleVentaEmpleadoDTO;

import java.util.List;

public interface AdministradorServicio {

    List<String> obtenerEmpleados();
    int registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO)throws Exception;
    String modificarEmpleado(ModificarEmpleadoAdminDTO empleadoDTO) throws Exception;
    boolean eliminarCuentaEmpleado(String cedula)throws Exception;
    List<ItemEmpleadoDTO> encontrarEmpleadosCedulaNombre() throws Exception;
    int agregarProducto(AgregarProductoDTO agregarProductoDTO) throws Exception;
    List<ItemRegistroProductoDTO> listarRegistroProductos();
    List<ItemVentaEmpleadoDTO> listaVentasEmpleados();
    DetalleVentaEmpleadoDTO verDetalleVentaEmpleado(int codigoVenta) throws Exception;
    DetalleRegistroProductoDTO verDetalleRegistro(int codigoRegistro) throws Exception;

}
