package co.ventanilla_gimli.dto;

import java.time.LocalDate;

public record ItemVentaEmpleadoDTO(
        int codigoVenta,
        LocalDate fechaVenta,
        String horaVenta,
        String nombreEmpleado,
        int codigoEmpleado
) {
}
