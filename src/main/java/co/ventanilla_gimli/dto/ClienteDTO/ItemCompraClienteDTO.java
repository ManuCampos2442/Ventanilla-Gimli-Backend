package co.ventanilla_gimli.dto.ClienteDTO;

import java.time.LocalDate;

public record ItemCompraClienteDTO(
        int codigo,
        String nombreProducto,
        int cantidad,
        LocalDate fechaVenta
) {
}
