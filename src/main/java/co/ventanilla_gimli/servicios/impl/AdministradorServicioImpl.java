package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.*;
import co.ventanilla_gimli.dto.AdministradorDTO.ItemEmpleadoDTO;
import co.ventanilla_gimli.dto.AdministradorDTO.ModificarEmpleadoAdminDTO;
import co.ventanilla_gimli.dto.ClienteDTO.DetalleVentaEmpleadoDTO;
import co.ventanilla_gimli.model.*;
import co.ventanilla_gimli.repositorios.*;
import co.ventanilla_gimli.servicios.interfaces.AdministradorServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministradorServicioImpl implements AdministradorServicio {

    private  final ClienteRepo clienteRepo;
    private  final EmpleadoRepo empleadoRepo;
    private  final AdministradorRepo administradorRepo;
    private final ProductoRepo productoRepo;
    private final VentaEmpleadoRepo ventaEmpleadoRepo;
    private final RegistroProductosRepo registroProductosRepo;

    @Override
    public List<String> obtenerEmpleados() {

        List<String> cedulasARetornar = new ArrayList<>();
        List<Empleado> empleados = empleadoRepo.findAll();
        // Si la categoría es ALCOHOL, obtenemos los nombres de alcohol de todos los productos

        for (Empleado e : empleados){
            cedulasARetornar.add(e.getCedula());
        }

        return cedulasARetornar;
    }

    @Override
    public int registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO) throws Exception {

        if(correoRepetido(registroEmpleadoDTO.correo())){
            throw  new Exception("El correo digitado ya se encuentra en uso");
        }

        Empleado empleado = new Empleado();
        empleado.setEstado(true);
        empleado.setNombre(registroEmpleadoDTO.nombre());
        empleado.setCedula(registroEmpleadoDTO.cedula());
        empleado.setTelefono(registroEmpleadoDTO.telefono());
        empleado.setCorreo(registroEmpleadoDTO.correo());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(registroEmpleadoDTO.password());

        empleado.setPassword(passwordEncriptada);

        empleadoRepo.save(empleado);

        return empleado.getCodigo();
    }

    @Override
    public String modificarEmpleado(ModificarEmpleadoAdminDTO empleadoDTO) throws Exception {

        Empleado empleadoEncontrado = empleadoRepo.findByCedula(empleadoDTO.cedulaPrevia());

        if(empleadoEncontrado == null){
            throw new Exception("No se encontro al empleado");
        }

        Empleado empleadoNuevo = empleadoEncontrado;
        empleadoNuevo.setCedula(empleadoDTO.cedulaNueva());
        empleadoNuevo.setNombre(empleadoDTO.nombre());
        empleadoNuevo.setTelefono(empleadoDTO.telefono());
        empleadoNuevo.setCorreo(empleadoDTO.correo());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(empleadoDTO.password());

        empleadoNuevo.setPassword(passwordEncriptada);
        empleadoRepo.save(empleadoNuevo);

        return empleadoNuevo.getCedula();
    }

    private boolean correoRepetido(String correo) {

        Cliente correoCliente = clienteRepo.findClienteByCorreo(correo);
        Empleado correoEmpleado = empleadoRepo.findByCorreo(correo);
        Administrador correoAdministrador = administradorRepo.findByCorreo(correo);

        if(correoCliente != null){
            return true;
        }
        if(correoEmpleado != null){
            return true;
        }
        if(correoAdministrador != null){
            return true;
        }

        return false;
    }

    @Override
    public boolean eliminarCuentaEmpleado(String cedula) throws Exception {
        Empleado clienteEncontrado = empleadoRepo.findByCedula(cedula);

        // Desvincular todas las ventas asociadas al cliente
        ventaEmpleadoRepo.desvincularVentasDelEmpleado(clienteEncontrado);

        // Desvincular todos los registros de productos asociados al cliente
        ventaEmpleadoRepo.desvincularRegistrosDeEmpleado(clienteEncontrado);

        // Finalmente, eliminar el cliente
        empleadoRepo.delete(clienteEncontrado);

        return true;
    }

    @Override
    public List<ItemEmpleadoDTO> encontrarEmpleadosCedulaNombre() throws Exception {

        List<Empleado> empleados = empleadoRepo.findAll();
        List<ItemEmpleadoDTO> empleadoARetornar = new ArrayList<>();

        for (Empleado e : empleados){
            empleadoARetornar.add(new ItemEmpleadoDTO(
                    e.getNombre(),
                    e.getCorreo(),
                    e.getCedula()
            ));
        }

        return empleadoARetornar;
    }

    @Override
    public int agregarProducto(AgregarProductoDTO agregarProductoDTO) throws Exception {

        Categoria categoria = agregarProductoDTO.categoria();
        if (categoria != null) {
            if (categoria.equals(Categoria.ALCOHOL)) {
                Producto productoEncontrado = productoRepo.findByNombresAlcohol(agregarProductoDTO.nombre());

                int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
                productoEncontrado.setCantidad(nuevaCantidad);

                // Guardar el producto actualizado en la base de datos
                productoRepo.save(productoEncontrado);
                hacerRegistroDeAgregacion(productoEncontrado, agregarProductoDTO.codigoEmpleado(), agregarProductoDTO.nombre(), agregarProductoDTO.cantidad());

            } else if (categoria.equals(Categoria.DULCES)) {
                Producto productoEncontrado = productoRepo.findByNombresDulces(agregarProductoDTO.nombre());

                int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
                productoEncontrado.setCantidad(nuevaCantidad);

                productoRepo.save(productoEncontrado);
                hacerRegistroDeAgregacion(productoEncontrado, agregarProductoDTO.getCodigoEmpleado(), agregarProductoDTO.nombre(), agregarProductoDTO.cantidad());

            } else {
                Producto productoEncontrado = productoRepo.findByNombresGaseosas(agregarProductoDTO.nombre());

                int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
                productoEncontrado.setCantidad(nuevaCantidad);

                productoRepo.save(productoEncontrado);
                hacerRegistroDeAgregacion(productoEncontrado, agregarProductoDTO.codigoEmpleado(), agregarProductoDTO.nombre(), agregarProductoDTO.cantidad());
            }

        }

        return 0;
    }

    public void hacerRegistroDeAgregacion(Producto producto, int codigoEmpleado, String nombreProducto, int cantidad){

        Administrador administrador = new Administrador();
        administrador.setCodigo(100);

        Optional<Empleado> empleadoEncontrado = empleadoRepo.findById(100);

        Empleado empleadoAdmin = new Empleado();

        RegistroProducto r = new RegistroProducto();
        r.setProducto(producto);
        r.setEmpleado(empleadoEncontrado.get());
        r.setCategoria(producto.getCategoria());
        r.setSubcategoria(producto.getSubcategoria());
        r.setNombreProducto(nombreProducto);
        r.setCantidad(cantidad);

        // Establecer la fecha actual
        LocalDate fechaActual = LocalDate.now();
        r.setFechaRegistro(fechaActual);

        // Establecer la hora actual en formato de cadena (String)
        LocalTime horaActual = LocalTime.now();
        String horaActualString = horaActual.toString(); // Convertir a formato de cadena
        r.setHoraDeRegistro(horaActualString);

        registroProductosRepo.save(r);

    }

    @Override
    public List<ItemRegistroProductoDTO> listarRegistroProductos() {

        List<RegistroProducto> registrosEncontrados = registroProductosRepo.findAll();
        List<ItemRegistroProductoDTO> registros = new ArrayList<>();

        for (RegistroProducto r : registrosEncontrados) {
            if (r.getEmpleado() != null) {
                registros.add(new ItemRegistroProductoDTO(
                        r.getCodigo(),
                        r.getNombreProducto(),
                        r.getCategoria(),
                        r.getEmpleado().getCodigo(),
                        r.getFechaRegistro()
                ));
            } else {
                // Manejar la situación donde el empleado asociado a este registro es null
                // Por ejemplo, podrías asignar un valor predeterminado para el código de empleado
                registros.add(new ItemRegistroProductoDTO(
                        r.getCodigo(),
                        r.getNombreProducto(),
                        r.getCategoria(),
                        100,
                        r.getFechaRegistro()
                ));
            }
        }


        return registros;
    }

    @Override
    public List<ItemVentaEmpleadoDTO> listaVentasEmpleados() {

        List<VentaEmpleado> ventas = ventaEmpleadoRepo.findAll();
        List<ItemVentaEmpleadoDTO> ventasARetornar = new ArrayList<>();

        for (VentaEmpleado v : ventas){
            if(v.getEmpleado() == null){
                v.setEmpleado(new Empleado());
            }
            ventasARetornar.add(new ItemVentaEmpleadoDTO(
                            v.getCodigo(),
                            v.getFechaVenta(),
                            v.getHoraDeVenta(),
                            v.getEmpleado().getNombre(),
                            v.getEmpleado().getCodigo()
                    )
            );
        }

        return ventasARetornar;
    }

    @Override
    @Transactional
    public DetalleVentaEmpleadoDTO verDetalleVentaEmpleado(int codigoVenta) throws Exception {

        Optional<VentaEmpleado> ventaEncontrada = ventaEmpleadoRepo.findById(codigoVenta);
        if (!ventaEncontrada.isPresent()) {
            throw new Exception("Venta no encontrada");
        }
        VentaEmpleado venta = ventaEncontrada.get();

        String nombreCliente = (venta.getCliente() != null) ? venta.getCliente().getNombre() : "Cliente casual";
        String nombreProducto = encontrarProductoPorCodigo(venta.getProducto().getCodigo());

        Empleado empleado = venta.getEmpleado();
        int codigoEmpleado;
        String nombreEmpleado;

        if (empleado == null) {
            // Si el empleado es null, usar 100 como el código del empleado
            codigoEmpleado = 100;
            nombreEmpleado = "Desconocido o empleado eliminado";
        } else {
            codigoEmpleado = empleado.getCodigo();
            nombreEmpleado = empleado.getNombre();
        }

        return new DetalleVentaEmpleadoDTO(
                venta.getCodigo(),
                venta.getFechaVenta(),
                venta.getHoraDeVenta(),
                nombreEmpleado, // Usar la variable nombreEmpleado
                codigoEmpleado, // Usar la variable codigoEmpleado
                nombreCliente, // Aquí se asigna "Cliente casual" si el cliente es nulo
                (venta.getCliente() != null) ? venta.getCliente().getCorreo() : null, // Se mantiene el correo si no es nulo
                nombreProducto,
                venta.getProducto().getCodigo()
        );
    }


    public String encontrarProductoPorCodigo(int codigo) throws Exception {

        Optional<Producto> productoEncontrado = productoRepo.findById(codigo);

        if(productoEncontrado.get().getCategoria().equals(Categoria.ALCOHOL)){
            for(String n : productoEncontrado.get().getNombresAlcohol()){
                return n;
            }
        }
        if(productoEncontrado.get().getCategoria().equals(Categoria.DULCES)){
            for(String n : productoEncontrado.get().getNombresDulces()){
                return n;
            }
        }else{
            for(String n : productoEncontrado.get().getNombresGaseosas()){
                return n;
            }
        }
        return null;
    }

    @Override
    public DetalleRegistroProductoDTO verDetalleRegistro(int codigoRegistro) throws Exception {

        Optional<RegistroProducto> registroEncontrado = registroProductosRepo.findById(codigoRegistro);

        if(registroEncontrado.isEmpty()){
            throw new Exception("No se pudo encontrar el registro");
        }

        RegistroProducto registro = registroEncontrado.get();

        // Verificar si el empleado es null
        Empleado empleado = registro.getEmpleado();
        int codigoEmpleado;
        String nombreEmpleado;

        if(empleado == null){
            // Si el empleado es null, usar 100 como el código del empleado
            codigoEmpleado = 100;
            nombreEmpleado = "Desconocido o empleado eliminado";
        } else {
            codigoEmpleado = empleado.getCodigo();
            nombreEmpleado = empleado.getNombre();
        }

        return new DetalleRegistroProductoDTO(
                registro.getProducto().getCodigo(),
                registro.getNombreProducto(),
                registro.getCantidad(),
                registro.getCategoria(),
                registro.getSubcategoria(),
                registro.getFechaRegistro(),
                registro.getHoraDeRegistro(),

                codigoEmpleado,
                nombreEmpleado
        );
    }
}
