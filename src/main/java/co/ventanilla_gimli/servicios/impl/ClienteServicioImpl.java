package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.*;
import co.ventanilla_gimli.dto.ClienteDTO.DetalleCompraClienteDTO;
import co.ventanilla_gimli.dto.ClienteDTO.ItemCompraClienteDTO;
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
    private final DetalleVentaClienteRepo detalleVentaClienteRepo;

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
    public List<ItemCompraClienteDTO> comprasRealizadas(int codigoCliente) {

        String correoCliente = correoCliente(codigoCliente);

        List<DetalleVentaCliente> compras = detalleVentaClienteRepo.findAllByCorreoCliente(correoCliente);
        List<ItemCompraClienteDTO> comprasARetornar = new ArrayList<>();

        for (DetalleVentaCliente d : compras){
            comprasARetornar.add(new ItemCompraClienteDTO(
                    d.getCodigo(),
                    d.getNombreProducto(),
                    d.getCantidad(),
                    d.getFechaVenta()
            ));
        }

        return comprasARetornar;
    }

    private String correoCliente(int codigoCliente) {
        Optional<Cliente> clienteEncontrado = clienteRepo.findById(codigoCliente);
        return clienteEncontrado.get().getCorreo();
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
    public DetalleCompraClienteDTO verDetalleCompra(int codigoCompra) throws Exception {

        Optional<DetalleVentaCliente> compra = detalleVentaClienteRepo.findById(codigoCompra);

        return new DetalleCompraClienteDTO(
                compra.get().getCodigo(),
                compra.get().getNombreProducto(),
                compra.get().getDescripcion(),
                compra.get().getPrecio(),
                compra.get().getCantidad(),
                compra.get().getFechaVenta(),
                compra.get().getHoraDeVenta(),
                compra.get().getDireccion(),
                compra.get().getDevueltas(),
                compra.get().getNombreCliente(),
                compra.get().getCorreoCliente(),
                compra.get().getTelefono(),
                compra.get().getVenta().getCodigo()
        );
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

    private boolean comprobacionPrecioCantidad(int cantidad, double precio) {
        if(precio <= 1500){
            if(cantidad <= 10){
                return  true;
            }
        }
        return  false;
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
        if(comprobacionPrecioCantidad(registroCompraClienteDTO.cantidad(), productoEncontrado.getPrecio()) == true){
            throw  new Exception("Para realizar la compra, debe adquirir mas de 10 productos");
        }
        double dineroADevolver = totalAPagar - registroCompraClienteDTO.dinero();
        if(registroCompraClienteDTO.cantidad() > productoEncontrado.getCantidad()){
            int cantidadARestar = (productoEncontrado.getCantidad() - registroCompraClienteDTO.cantidad()) * -1;
            throw  new Exception("No hay tanta cantidad de producto, si desea puede restarle " + cantidadARestar + " a la cantidad de productos");
        }
        if(registroCompraClienteDTO.cantidad() <= 0){
            throw  new Exception("La cantidad a comprar no puede ser menor o igual a 0");
        }

        VentaCliente venta = new VentaCliente();

        venta.setProducto(productoEncontrado);
        venta.setCliente(clienteEncontrado.get());
        venta.setDireccion(registroCompraClienteDTO.direccion());
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

        hacerRegistroVenta(registroCompraClienteDTO.nombreProducto(), productoEncontrado,
                registroCompraClienteDTO.cantidad(), fechaActual, horaActualString, registroCompraClienteDTO.direccion(),
                dineroADevolver, venta);

        emailServicio.enviarCorreo(new EmailDTO(
                clienteEncontrado.get().getCorreo(),
                "Se ha registrado la compra con éxito",
                "La compra del producto " + registroCompraClienteDTO.nombreProducto()+ " ha sido un" +
                        " exito, gracias por comprar en la Ventanilla Gimli. La cantidad de producto comprado fue de: " + registroCompraClienteDTO.cantidad() +  "." +
                        " Sus devueltas son: " + (dineroADevolver)*(-1) + "$" + "\n" +
                        " - Fecha de la venta: " + fechaActual + "\n" +
                        " - Hora de la venta: " + horaActual + "\n" +
                        " - Correo del cliente: " + clienteEncontrado.get().getCorreo() + "\n" +
                        " - Direccion del cliente: " + registroCompraClienteDTO.direccion()

        ));

        return ventaNueva.getCodigo();
    }

    private void hacerRegistroVenta(String nombreProducto, Producto productoEncontrado, int cantidad,
                                    LocalDate fechaActual, String horaActual, String direccion, double dineroADevolver, VentaCliente venta) {

        DetalleVentaCliente Detalleventa = new DetalleVentaCliente();
        Detalleventa.setNombreProducto(nombreProducto);
        Detalleventa.setDescripcion(productoEncontrado.getDescripcion());
        Detalleventa.setPrecio( productoEncontrado.getPrecio());
        Detalleventa.setCantidad(cantidad);
        Detalleventa.setFechaVenta(fechaActual);
        Detalleventa.setHoraDeVenta(horaActual);
        Detalleventa.setDireccion(direccion);
        Detalleventa.setDevueltas(dineroADevolver * (-1));
        Detalleventa.setNombreCliente(venta.getCliente().getNombre());
        Detalleventa.setCorreoCliente(venta.getCliente().getCorreo());
        Detalleventa.setTelefono(venta.getCliente().getTelefono());
        Detalleventa.setVenta(venta);

        detalleVentaClienteRepo.save(Detalleventa);

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
