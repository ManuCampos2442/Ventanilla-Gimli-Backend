package co.ventanilla_gimli.controladores;

import co.ventanilla_gimli.dto.LoginDTO;
import co.ventanilla_gimli.dto.ClienteDTO.RegistroClienteDTO;
import co.ventanilla_gimli.dto.TokenDTO.MensajeDTO;
import co.ventanilla_gimli.dto.TokenDTO.TokenDTO;
import co.ventanilla_gimli.servicios.interfaces.AutenticacionServicio;
import co.ventanilla_gimli.servicios.interfaces.ClienteServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticacionController {

    private final AutenticacionServicio autenticacionServicio;
    private final ClienteServicio clienteServicio;

    @PostMapping("/login")
    public ResponseEntity<MensajeDTO<TokenDTO>> login(@Valid @RequestBody LoginDTO loginDTO)
            throws Exception {

        TokenDTO tokenDTO = autenticacionServicio.login(loginDTO);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, tokenDTO));
    }

    @PostMapping("/registrar-cliente")
    public ResponseEntity<MensajeDTO<String>> registrarCliente(@Valid @RequestBody RegistroClienteDTO cliente) throws Exception{
        clienteServicio.registrarCliente(cliente);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Cliente registrado correctamente"));
    }
}
