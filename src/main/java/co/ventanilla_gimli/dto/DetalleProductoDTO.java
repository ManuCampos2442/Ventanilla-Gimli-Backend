package co.ventanilla_gimli.dto;

import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Subcategoria;

public record DetalleProductoDTO(
        int idProducto,
        String nombreProducto,
        String descripcion,
        double precio,
        int cantidad,
        Categoria categoria,
        Subcategoria subcategoria,
        String proveedor
) {
}
