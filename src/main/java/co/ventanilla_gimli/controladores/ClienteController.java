package co.ventanilla_gimli.controladores;

import co.ventanilla_gimli.dto.*;
import co.ventanilla_gimli.dto.ClienteDTO.ModificarClienteDTO;
import co.ventanilla_gimli.dto.TokenDTO.MensajeDTO;
import co.ventanilla_gimli.servicios.interfaces.ClienteServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteServicio clienteServicio;

    @GetMapping("/listar-productos")
    public ResponseEntity<MensajeDTO<List<ItemProductoDTO>>> listarProductos() throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                clienteServicio.listarProductos()));
    }

    @GetMapping("/filtar-productos-nombre/{nombreProducto}")
    public ResponseEntity<MensajeDTO<FiltroBusquedaDTO>> filtrarProductoPorNombre(@PathVariable String nombreProducto) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                clienteServicio.filtrarProductoPorNombre(nombreProducto)));
    }

    @PostMapping("/registrar-compra")
    public ResponseEntity<MensajeDTO<String>> registrarCompraCliente(@Valid @RequestBody RegistroCompraClienteDTO registroCompraClienteDTO) throws Exception{
        clienteServicio.realizarCompra(registroCompraClienteDTO);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Compra registrada correctamente"));
    }

    @DeleteMapping("/eliminar-cuenta/{codigo}")
    public ResponseEntity<MensajeDTO<String>> eliminarCuenta(@PathVariable int codigo) throws
            Exception{
        clienteServicio.eliminarCuenta(codigo);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Cliente eliminado correctamete")
        );
    }

    @PutMapping("/editar-perfil")
    public ResponseEntity<MensajeDTO<String>> editarPerfil(@Valid @RequestBody ModificarClienteDTO clienteDTO) throws Exception{
        clienteServicio.modificarCliente(clienteDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Cliente actualizado " +
                "correctamete") );
    }

    @GetMapping("/detalle-producto/{codigoProducto}")
    public ResponseEntity<MensajeDTO<DetalleProductoDTO>> verDetalleRegistro(@PathVariable int codigoProducto) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                clienteServicio.verDetalleProducto(codigoProducto)));
    }
}
