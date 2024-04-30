package co.ventanilla_gimli.dto.AdministradorDTO;

public record ModificarEmpleadoAdminDTO(
        String nombre,
        String telefono,
        String cedulaPrevia,
        String cedulaNueva,
        String correo,
        String password
) {
}
