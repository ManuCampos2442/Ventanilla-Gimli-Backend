package co.ventanilla_gimli.dto;

import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Subcategoria;

import java.time.LocalDate;

public record RegistroAgregacionProductoDTO (
        Integer codigo,
        String nombreProducto,
        Categoria categoria,
        Subcategoria subcategoria,
        LocalDate fechaRegistro,
        String horaDeRegistro,
        Integer idProducto,
        Integer idAdmin,
        Integer idEmpleado
) {
    public RegistroAgregacionProductoDTO {
        if (codigo == null) {
            codigo = 0;
        }
        if (idProducto == null) {
            idProducto = 0;
        }
        if (idAdmin == null) {
            idAdmin = 0;
        }
        if (idEmpleado == null) {
            idEmpleado = 0;
        }
    }
}

