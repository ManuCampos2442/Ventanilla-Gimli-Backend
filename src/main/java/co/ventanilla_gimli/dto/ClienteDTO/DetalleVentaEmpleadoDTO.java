package co.ventanilla_gimli.dto.ClienteDTO;

import java.time.LocalDate;

public record DetalleVentaEmpleadoDTO(

        int codigoVenta,
        LocalDate fechaVenta,
        String horaVenta,
        String nombreEmpleado,
        int codigoEmpleado,
        String nombreCliente,
        String correoCliente,
        String nombreProducto,
        int codigoProducto

) {
}
