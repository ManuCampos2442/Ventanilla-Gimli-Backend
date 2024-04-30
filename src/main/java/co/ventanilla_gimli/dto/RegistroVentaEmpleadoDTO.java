package co.ventanilla_gimli.dto;

import java.time.LocalDate;

public record RegistroVentaEmpleadoDTO(
        LocalDate fechaVenta,
        String horaDeVenta,
        int cantidad,
        double precioUnitario,
        String nombreProducto,
        int codigoCliente,
        int codigoEmpleado,
        String correoCliente,
        double dinero

) {
}
