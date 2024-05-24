package co.ventanilla_gimli.dto.ClienteDTO;

import java.time.LocalDate;

public record DetalleCompraClienteDTO(
        int codigo,
        String nombreProducto,
        String descripcion,
        double precio,
        int cantidad,
        LocalDate fechaVenta,
        String horaDeVenta,
        String direccion,
        double devueltas,
        String nombreCliente,
        String correoCliente,
        String telefono,
        int  venta
) {
}
