package co.ventanilla_gimli.controladores;

import co.ventanilla_gimli.dto.*;
import co.ventanilla_gimli.dto.ClienteDTO.DetalleVentaEmpleadoDTO;
import co.ventanilla_gimli.dto.DetalleRegistroProductoDTO;
import co.ventanilla_gimli.dto.TokenDTO.MensajeDTO;
import co.ventanilla_gimli.servicios.interfaces.EmpleadoServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoServicio empleadoServicio;


    @GetMapping("/obtener-codigo-cliente/{correo}")
    public ResponseEntity<MensajeDTO<Integer>> obtenerClientePorCorreo(@PathVariable String correo) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                empleadoServicio.encontrarClientePorCorreo(correo)));
    }
    @PostMapping("/agregar-producto")
    public ResponseEntity<MensajeDTO<String>> agregarProducto(@Valid @RequestBody AgregarProductoDTO agregarProductoDTO) throws Exception{
            empleadoServicio.agregarProducto(agregarProductoDTO);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Producto agregado correctamente"));
    }
    @GetMapping("/listar-registros-agregacion-productos")
    public ResponseEntity<MensajeDTO<List<ItemRegistroProductoDTO>>> listarRegistrosAgreacionProductos() throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                empleadoServicio.listarRegistroProductos()));
    }
    @GetMapping("/listar-ventas-empleados")
    public ResponseEntity<MensajeDTO<List<ItemVentaEmpleadoDTO>>> listaVentasEmpleados() throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                empleadoServicio.listaVentasEmpleados()));
    }

    @GetMapping("/detalle-registro/{codigoRegistro}")
    public ResponseEntity<MensajeDTO<DetalleRegistroProductoDTO>> verDetalleRegistro(@PathVariable int codigoRegistro) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                empleadoServicio.verDetalleRegistro(codigoRegistro)));
    }

    @GetMapping("/detalle-venta-empleado/{codigoVenta}")
    public ResponseEntity<MensajeDTO<DetalleVentaEmpleadoDTO>> verDetalleVentaEmpleado(@PathVariable int codigoVenta) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                empleadoServicio.verDetalleVentaEmpleado(codigoVenta)));
    }

}
