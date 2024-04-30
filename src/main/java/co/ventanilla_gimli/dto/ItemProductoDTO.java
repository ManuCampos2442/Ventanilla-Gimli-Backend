package co.ventanilla_gimli.dto;

import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Subcategoria;

public record ItemProductoDTO(
        int codigo,
        Categoria categoria,
        Subcategoria subcategoria,
        String nombreProducto,
        double precio,
        String proveedor


){
}
