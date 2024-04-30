package co.ventanilla_gimli.controladores;

import co.ventanilla_gimli.dto.AdministradorDTO.ModificarEmpleadoAdminDTO;
import co.ventanilla_gimli.dto.ClienteDTO.ModificarClienteDTO;
import co.ventanilla_gimli.dto.RegistroEmpleadoDTO;
import co.ventanilla_gimli.dto.TokenDTO.MensajeDTO;
import co.ventanilla_gimli.model.Subcategoria;
import co.ventanilla_gimli.servicios.interfaces.AdministradorServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorServicio administradorServicio;

    @PostMapping("/registrar-empleado")
    public ResponseEntity<MensajeDTO<String>> registrarEmpleado(@Valid @RequestBody RegistroEmpleadoDTO empleado) throws Exception {
        administradorServicio.registrarEmpleado(empleado);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Empleado registrado correctamente"));
    }

    @GetMapping("/lista-cedulas-empleados")
    public ResponseEntity<MensajeDTO<List<String>>> listarCedulas(){
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                administradorServicio.obtenerEmpleados()));
    }

    @PutMapping("/editar-perfil-empleado")
    public ResponseEntity<MensajeDTO<String>> editarPerfilEmpleado(@Valid @RequestBody ModificarEmpleadoAdminDTO empleadoDTO) throws Exception{
        administradorServicio.modificarEmpleado(empleadoDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Empleado actualizado " +
                "correctamete") );
    }

    @DeleteMapping("/eliminar-cuenta-empleado/{cedula}")
    public ResponseEntity<MensajeDTO<String>> eliminarCuenta(@PathVariable String cedula) throws
            Exception{
        administradorServicio.eliminarCuentaEmpleado(cedula);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Empleado eliminado correctamete")
        );
    }

}
