package co.ventanilla_gimli.dto;

public record RegistroCompraClienteDTO(
        int cantidad,
        String nombreProducto,
        String direccion,
        int codigoCliente,
        double dinero
) {
}
