package co.ventanilla_gimli.servicios.interfaces;

import co.ventanilla_gimli.dto.AgregarProductoDTO;
import co.ventanilla_gimli.dto.ClienteDTO.DetalleVentaEmpleadoDTO;
import co.ventanilla_gimli.dto.ItemRegistroProductoDTO;
import co.ventanilla_gimli.dto.DetalleRegistroProductoDTO;
import co.ventanilla_gimli.dto.ItemVentaEmpleadoDTO;

import java.util.List;

public interface EmpleadoServicio {

    int encontrarClientePorCorreo(String correo)throws Exception;
    int agregarProducto(AgregarProductoDTO agregarProductoDTO) throws Exception;
    List<ItemRegistroProductoDTO> listarRegistroProductos();
    DetalleRegistroProductoDTO verDetalleRegistro(int codigoRegistro) throws Exception;
    List<ItemVentaEmpleadoDTO> listaVentasEmpleados();
    DetalleVentaEmpleadoDTO verDetalleVentaEmpleado(int codigoVenta) throws Exception;

}
