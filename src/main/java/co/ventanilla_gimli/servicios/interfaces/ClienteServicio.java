package co.ventanilla_gimli.servicios.interfaces;

import co.ventanilla_gimli.dto.*;
import co.ventanilla_gimli.dto.ClienteDTO.ModificarClienteDTO;
import co.ventanilla_gimli.dto.ClienteDTO.RegistroClienteDTO;

import java.util.List;

public interface ClienteServicio {
    int registrarCliente(RegistroClienteDTO registroClienteDTO) throws Exception;
    int modificarCliente(ModificarClienteDTO modificarClienteDTO) throws Exception;
    boolean eliminarCuenta(int codigoCliente)throws Exception;
    List<ItemProductoDTO> listarProductos();
    FiltroBusquedaDTO filtrarProductoPorNombre(String nombreProducto);
    int realizarCompra(RegistroCompraClienteDTO registroCompraClienteDTO) throws Exception;
    DetalleProductoDTO verDetalleProducto(int codigoProducto) throws Exception;

}
