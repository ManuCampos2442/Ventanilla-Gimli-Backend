package co.ventanilla_gimli.dto.ClienteDTO;

public record ModificarClienteDTO(

        int codigoCliente,
        String nombre,
        String telefono,
        String direccion,
        String correo,
        String password) {

}
