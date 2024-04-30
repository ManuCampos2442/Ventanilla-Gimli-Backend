package co.ventanilla_gimli.dto;

import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Subcategoria;

import java.util.List;

public record RegistroProductoDTO(
        Categoria categoria,
        Subcategoria subcategoria,
        String nombre,
        String proveedor,
        String descripcion,
        double precio,
        int cantidad,
       List<String> nombresAlcohol
) {
}
