package co.ventanilla_gimli.controladores;

import co.ventanilla_gimli.dto.*;
import co.ventanilla_gimli.dto.AdministradorDTO.ItemEmpleadoDTO;
import co.ventanilla_gimli.dto.AdministradorDTO.ModificarEmpleadoAdminDTO;
import co.ventanilla_gimli.dto.ClienteDTO.DetalleVentaEmpleadoDTO;
import co.ventanilla_gimli.dto.ClienteDTO.ModificarClienteDTO;
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

    @GetMapping("/lista-empleados-cedula-nombre")
    public ResponseEntity<MensajeDTO<List<ItemEmpleadoDTO>>> listarEmpleadoCedulaNombre() throws Exception {
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                administradorServicio.encontrarEmpleadosCedulaNombre()));
    }

    @PostMapping("/agregar-producto")
    public ResponseEntity<MensajeDTO<String>> agregarProducto(@Valid @RequestBody AgregarProductoDTO agregarProductoDTO) throws Exception{
        administradorServicio.agregarProducto(agregarProductoDTO);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Producto agregado correctamente"));
    }

    @GetMapping("/listar-registros-agregacion-productos")
    public ResponseEntity<MensajeDTO<List<ItemRegistroProductoDTO>>> listarRegistrosAgreacionProductos() throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                administradorServicio.listarRegistroProductos()));
    }

    @GetMapping("/listar-ventas-empleados")
    public ResponseEntity<MensajeDTO<List<ItemVentaEmpleadoDTO>>> listaVentasEmpleados() throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                administradorServicio.listaVentasEmpleados()));
    }

    @GetMapping("/detalle-registro/{codigoRegistro}")
    public ResponseEntity<MensajeDTO<DetalleRegistroProductoDTO>> verDetalleRegistro(@PathVariable int codigoRegistro) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                administradorServicio.verDetalleRegistro(codigoRegistro)));
    }

    @GetMapping("/detalle-venta-empleado/{codigoVenta}")
    public ResponseEntity<MensajeDTO<DetalleVentaEmpleadoDTO>> verDetalleVentaEmpleado(@PathVariable int codigoVenta) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                administradorServicio.verDetalleVentaEmpleado(codigoVenta)));
    }

}
