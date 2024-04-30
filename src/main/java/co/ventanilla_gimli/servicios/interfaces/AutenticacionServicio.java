package co.ventanilla_gimli.servicios.interfaces;

import co.ventanilla_gimli.dto.LoginDTO;
import co.ventanilla_gimli.dto.TokenDTO.TokenDTO;

public interface AutenticacionServicio {
    TokenDTO login(LoginDTO loginDTO) throws Exception;

}
