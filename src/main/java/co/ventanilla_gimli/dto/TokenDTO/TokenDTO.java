package co.ventanilla_gimli.dto.TokenDTO;

import jakarta.validation.constraints.NotBlank;

public record TokenDTO(
        @NotBlank
        String token
         ){
}
