package co.ventanilla_gimli.dto;

import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Subcategoria;

public record AgregarProductoDTO(

        Categoria categoria,
        int cantidad,
        String nombre,
        int codigoEmpleado
) {
    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }
}
