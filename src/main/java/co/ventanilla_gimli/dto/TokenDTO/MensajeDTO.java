package co.ventanilla_gimli.dto.TokenDTO;

public record MensajeDTO<T>(
        boolean error,
        T respuesta) {
}
