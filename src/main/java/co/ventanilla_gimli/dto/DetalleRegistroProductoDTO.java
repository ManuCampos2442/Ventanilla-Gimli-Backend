package co.ventanilla_gimli.dto;

import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Subcategoria;

import java.time.LocalDate;

public record DetalleRegistroProductoDTO(
        int idProducto,
        String nombreProducto,
        int cantidad,
        Categoria categoria,
        Subcategoria subcategoria,
        LocalDate fechaRegistro,
        String horaDeRegistro,
        int idEmpleado,
        String nombreEmpleado
) {
}
