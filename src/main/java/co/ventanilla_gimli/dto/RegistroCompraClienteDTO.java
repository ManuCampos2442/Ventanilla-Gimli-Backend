package co.ventanilla_gimli.dto;

public record RegistroCompraClienteDTO(
        int cantidad,
        String nombreProducto,
        int codigoCliente,
        double dinero
) {
}
