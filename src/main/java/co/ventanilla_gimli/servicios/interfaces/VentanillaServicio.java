package co.ventanilla_gimli.servicios.interfaces;

import co.ventanilla_gimli.dto.AgregarProductoDTO;
import co.ventanilla_gimli.dto.RegistroProductoDTO;
import co.ventanilla_gimli.dto.RegistroVentaEmpleadoDTO;
import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Subcategoria;

import java.util.List;

public interface VentanillaServicio {

    List<Categoria> listarCategorias();
    List<Subcategoria> listarSubcategorias();

    List<String> listarNombresAlcoholes(Categoria categoria);
    List<String> listarNombresDulces(Categoria categoria);
    List<String> listarNombresGaseosas(Categoria categoria);

     int registrarProducto(RegistroProductoDTO registroProductoDTO) throws Exception;

     int registrarVentaEmpleado(RegistroVentaEmpleadoDTO registroVentaEmpleado) throws Exception;

    int agregarProducto(AgregarProductoDTO agregarProductoDTO);
}
