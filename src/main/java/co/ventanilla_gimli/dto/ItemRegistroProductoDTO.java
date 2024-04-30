package co.ventanilla_gimli.dto;

import co.ventanilla_gimli.model.Categoria;

import java.time.LocalDate;

public record ItemRegistroProductoDTO(
    int codigo,
    String nombreProducto,
    Categoria categoria,
    int idEmpleado,
    LocalDate fechaRegistro
) {
}
