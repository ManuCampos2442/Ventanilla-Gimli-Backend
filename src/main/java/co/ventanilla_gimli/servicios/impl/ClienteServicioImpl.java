package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.*;
import co.ventanilla_gimli.dto.ClienteDTO.ModificarClienteDTO;
import co.ventanilla_gimli.dto.ClienteDTO.RegistroClienteDTO;
import co.ventanilla_gimli.model.*;
import co.ventanilla_gimli.repositorios.*;
import co.ventanilla_gimli.servicios.interfaces.ClienteServicio;
import co.ventanilla_gimli.servicios.interfaces.EmailServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ClienteServicioImpl implements ClienteServicio{
    private  final ClienteRepo clienteRepo;
    private  final EmpleadoRepo empleadoRepo;
    private  final AdministradorRepo administradorRepo;
    private final ProductoRepo productoRepo;
    private final VentaEmpleadoRepo ventaEmpleadoRepo;
    private final VentaClienteRepo ventaClienteRepo;
    private final EmailServicio emailServicio;

    @Override
    public int registrarCliente(RegistroClienteDTO registroClienteDTO) throws Exception {

        if(correoRepetido(registroClienteDTO.correo())){
            throw  new Exception("El correo digitado ya se encuentra en uso");
        }

        Cliente cliente = new Cliente();
        cliente.setEstado(true);
        cliente.setNombre(registroClienteDTO.nombre());
        cliente.setTelefono(registroClienteDTO.telefono());
        cliente.setDireccion(registroClienteDTO.direccion());
        cliente.setCorreo(registroClienteDTO.correo());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(registroClienteDTO.password());

        cliente.setPassword(passwordEncriptada);

        clienteRepo.save(cliente);

        emailServicio.enviarCorreo(new EmailDTO(
                registroClienteDTO.correo(),
                "Registro Exitoso",
                "Felicidades, su registro en la ventanilla Gimli ha sido exitoso, bienvenido"
        ));

        return cliente.getCodigo();
    }

    @Override
    public int modificarCliente(ModificarClienteDTO modificarClienteDTO) throws Exception {

        Optional<Cliente> clienteEncontrado = clienteRepo.findById(modificarClienteDTO.codigoCliente());

        if(clienteEncontrado.isEmpty()){
            throw new Exception("El cliente no existe");
        }

        Cliente cliente = clienteEncontrado.get();

        cliente.setNombre(modificarClienteDTO.nombre());
        cliente.setTelefono(modificarClienteDTO.telefono());
        cliente.setDireccion(modificarClienteDTO.direccion());
        cliente.setCorreo(modificarClienteDTO.correo());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(modificarClienteDTO.password());

        cliente.setPassword(passwordEncriptada);

        clienteRepo.save(cliente);

        return cliente.getCodigo();
    }

    @Override
    public boolean eliminarCuenta(int codigoCliente) throws Exception {

       /* Optional<Cliente> clienteEncontrado = clienteRepo.findById(codigoCliente);

        Cliente cliente = clienteEncontrado.get();
       // cliente.setEstado(false);
        clienteRepo.delete(cliente);

       // clienteRepo.save(cliente);

        return true;*/

        Optional<Cliente> clienteEncontrado = clienteRepo.findById(codigoCliente);
        Cliente cliente = clienteEncontrado.orElseThrow(() -> new Exception("Cliente no encontrado"));

        // Desvincular todas las ventas asociadas al cliente estableciendo cliente_codigo en NULL
        ventaClienteRepo.desvincularVentasDelCliente(cliente.getCodigo());
        ventaClienteRepo.desvincularVentasDelCliente2(cliente.getCodigo());
        // Finalmente, eliminar el cliente
        clienteRepo.delete(cliente);

        return true;
    }

    @Override
    @Transactional
    public List<ItemProductoDTO> listarProductos() {

        List<Producto> productos = productoRepo.findAll();
        List<ItemProductoDTO> productoAREtornar = new ArrayList<>();

        for (Producto producto : productos) {
            for (String nombre : producto.getNombresAlcohol()) {
                if(producto.getCantidad() >= 1) {
                    productoAREtornar.add(new ItemProductoDTO(
                            producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor()
                    ));
                }
            }
            for (String nombre : producto.getNombresDulces()) {
                if (producto.getCantidad() >= 1){
                    productoAREtornar.add(new ItemProductoDTO(
                            producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor()
                    ));
                }
            }
            for (String nombre : producto.getNombresGaseosas()) {
                if(producto.getCantidad() >= 1) {
                    productoAREtornar.add(new ItemProductoDTO(
                            producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor()
                    ));
                }
            }
        }

        return productoAREtornar;
    }

    @Override
    public DetalleProductoDTO verDetalleProducto(int codigoProducto) throws Exception {

        Optional<Producto> productoEncontrado = productoRepo.findById(codigoProducto);


        if(productoEncontrado.get().getCategoria().equals(Categoria.ALCOHOL)){
            for(String nombre : productoEncontrado.get().getNombresAlcohol()){
               return new DetalleProductoDTO(
                       productoEncontrado.get().getCodigo(),
                       nombre,
                       productoEncontrado.get().getDescripcion(),
                       productoEncontrado.get().getPrecio(),
                       productoEncontrado.get().getCantidad(),
                       productoEncontrado.get().getCategoria(),
                       productoEncontrado.get().getSubcategoria(),
                       productoEncontrado.get().getProveedor()
               );
            }
        }
        if(productoEncontrado.get().getCategoria().equals(Categoria.DULCES)){
            for(String nombre : productoEncontrado.get().getNombresDulces()){
                return new DetalleProductoDTO(
                        productoEncontrado.get().getCodigo(),
                        nombre,
                        productoEncontrado.get().getDescripcion(),
                        productoEncontrado.get().getPrecio(),
                        productoEncontrado.get().getCantidad(),
                        productoEncontrado.get().getCategoria(),
                        productoEncontrado.get().getSubcategoria(),
                        productoEncontrado.get().getProveedor()
                );
            }
        }else{
            for(String nombre : productoEncontrado.get().getNombresGaseosas()){
                return new DetalleProductoDTO(
                        productoEncontrado.get().getCodigo(),
                        nombre,
                        productoEncontrado.get().getDescripcion(),
                        productoEncontrado.get().getPrecio(),
                        productoEncontrado.get().getCantidad(),
                        productoEncontrado.get().getCategoria(),
                        productoEncontrado.get().getSubcategoria(),
                        productoEncontrado.get().getProveedor()
                );
            }

        }



        return null;
    }

    @Override
    @Transactional
    public FiltroBusquedaDTO filtrarProductoPorNombre(String nombreProducto) {

       List<Producto> productos = productoRepo.findAll();

        for (Producto producto : productos) {
            for (String nombre : producto.getNombresAlcohol()) {
                if (nombre.equals(nombreProducto)) {
                    // Si encontramos una coincidencia, retornamos el producto
                    return new FiltroBusquedaDTO(producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor());
                }
            }
        }
        for (Producto producto : productos) {
            for (String nombre : producto.getNombresDulces()) {
                if (nombre.equals(nombreProducto)) {
                    // Si encontramos una coincidencia, retornamos el producto
                    return new FiltroBusquedaDTO(producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor());
                }
            }
        }
        for (Producto producto : productos) {
            for (String nombre : producto.getNombresGaseosas()) {
                if (nombre.equals(nombreProducto)) {
                    // Si encontramos una coincidencia, retornamos el producto
                    return new FiltroBusquedaDTO(producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor());
                }
            }
        }

        return null;
    }

    @Override
    public int realizarCompra(RegistroCompraClienteDTO registroCompraClienteDTO) throws Exception{

        Optional<Cliente> clienteEncontrado = clienteRepo.findById(registroCompraClienteDTO.codigoCliente());
        //Optional<Empleado> empleadoEncontado = empleadoRepo.findById(registroCompraClienteDTO.codigoEmpleado());
        Producto productoEncontrado = encontrarProductoPorNombre(registroCompraClienteDTO.nombreProducto());
        double totalAPagar = productoEncontrado.getPrecio() * registroCompraClienteDTO.cantidad();


        if(productoEncontrado.getCantidad() <= 0){
            throw  new Exception("Producto agotado, por favor intentelo mas tarde");
        }
        if(registroCompraClienteDTO.dinero() < totalAPagar){
            throw  new Exception("Dinero insuficiente");
        }
        double dineroADevolver = totalAPagar - registroCompraClienteDTO.dinero();
        if(registroCompraClienteDTO.cantidad() > productoEncontrado.getCantidad()){
            int cantidadARestar = (productoEncontrado.getCantidad() - registroCompraClienteDTO.cantidad()) * -1;
            throw  new Exception("No hay tanta cantidad de producto, si desea puede restarle " + cantidadARestar + " a la cantidad de productos");
        }

        VentaCliente venta = new VentaCliente();

        venta.setProducto(productoEncontrado);
        venta.setCliente(clienteEncontrado.get());
        venta.setCantidad(registroCompraClienteDTO.cantidad());
        venta.setPrecioUnitario(productoEncontrado.getPrecio());

        int nuevaCantidad = productoEncontrado.getCantidad() - registroCompraClienteDTO.cantidad();
        productoEncontrado.setCantidad(nuevaCantidad);

        // Establecer la fecha actual
        LocalDate fechaActual = LocalDate.now();
        venta.setFechaVenta(fechaActual);

        // Establecer la hora actual en formato de cadena (String)
        LocalTime horaActual = LocalTime.now();
        String horaActualString = horaActual.toString(); // Convertir a formato de cadena
        venta.setHoraDeVenta(horaActualString);



        VentaCliente ventaNueva = ventaClienteRepo.save(venta);
        productoRepo.save(productoEncontrado);

        emailServicio.enviarCorreo(new EmailDTO(
                clienteEncontrado.get().getCorreo(),
                "Se ha registrado la compra con éxito",
                "La compra del producto " + registroCompraClienteDTO.nombreProducto()+ " ha sido un" +
                        " exito, gracias por comprar en la Ventanilla Gimli. La cantidad de producto comprado fue de: " + registroCompraClienteDTO.cantidad() +  "." +
                        " Sus devueltas son: " + (dineroADevolver)*(-1) + "$"
        ));

        return ventaNueva.getCodigo();
    }

    private int generarNumeroAleatorio(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }


    public Producto encontrarProductoPorNombre(String nombre) throws Exception {

        Producto productoEncontradoAlcohol = productoRepo.findByNombresAlcohol(nombre);

        if(productoEncontradoAlcohol == null){

            Producto productoEncontradoGaseosas = productoRepo.findByNombresGaseosas(nombre);

            if(productoEncontradoGaseosas == null){

                Producto productoEncontradoDulces = productoRepo.findByNombresDulces(nombre);

                if(productoEncontradoDulces == null){
                    throw new Exception("Error con el producto");
                }else{
                    return productoRepo.findByNombresDulces(nombre);
                }

            }else{
                return productoEncontradoGaseosas;
            }
        }else{
            return productoEncontradoAlcohol;
        }
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
}
