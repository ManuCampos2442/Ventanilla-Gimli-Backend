package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.AgregarProductoDTO;
import co.ventanilla_gimli.dto.EmailDTO;
import co.ventanilla_gimli.dto.RegistroProductoDTO;
import co.ventanilla_gimli.dto.RegistroVentaEmpleadoDTO;
import co.ventanilla_gimli.model.*;
import co.ventanilla_gimli.repositorios.ClienteRepo;
import co.ventanilla_gimli.repositorios.EmpleadoRepo;
import co.ventanilla_gimli.repositorios.ProductoRepo;
import co.ventanilla_gimli.repositorios.VentaEmpleadoRepo;
import co.ventanilla_gimli.servicios.interfaces.EmailServicio;
import co.ventanilla_gimli.servicios.interfaces.VentanillaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VentanillaServicioImpl implements VentanillaServicio {

    private final ProductoRepo productoRepo;
    private final EmpleadoRepo empleadoRepo;
    private final ClienteRepo clienteRepo;
    private final VentaEmpleadoRepo ventaRepo;
    private final EmailServicio emailServicio;

    @Override
    public List<Categoria> listarCategorias() {
        return List.of(Categoria.values());
    }

    @Override
    public List<Subcategoria> listarSubcategorias() {
        return List.of(Subcategoria.values());
    }

    @Override
    public List<String> listarNombresAlcoholes(Categoria categoria) {

        List<String> nombresARetornar = new ArrayList<>();

        if (categoria.equals(Categoria.ALCOHOL)) {
            // Si la categoría es ALCOHOL, obtenemos los nombres de alcohol de todos los productos
            List<String> nombresAlcohol = productoRepo.findAllNombresAlcoholByCategoria(Categoria.ALCOHOL);

            // Agregamos los nombres de alcohol a la lista a retornar
            nombresARetornar.addAll(nombresAlcohol);
        }

        return nombresARetornar;
    }

    @Override
    public List<String> listarNombresDulces(Categoria categoria) {

        List<String> nombresARetornar = new ArrayList<>();

        if ((categoria.equals(Categoria.DULCES))){
            List<String> nombresDulces = productoRepo.findAllNombresDulcesByCategoria(categoria);

            nombresARetornar.addAll(nombresDulces);
        }

        return nombresARetornar;
    }

    @Override
    public List<String> listarNombresGaseosas(Categoria categoria) {

        List<String> nombresARetornar = new ArrayList<>();

        if ((categoria.equals(Categoria.GASEOSA))){
            List<String> nombresGaseosas = productoRepo.findAllNombresGaseosasByCategoria(categoria);

            nombresARetornar.addAll(nombresGaseosas);
        }

        return nombresARetornar;
    }


    @Override
    public int registrarVentaEmpleado(RegistroVentaEmpleadoDTO registroVentaEmpleado) throws Exception {

        VentaEmpleado venta = new VentaEmpleado();
        Optional<Empleado> empleado = empleadoRepo.findById(registroVentaEmpleado.codigoEmpleado());
        Optional<Cliente> cliente = clienteRepo.findById(registroVentaEmpleado.codigoCliente());
        Producto producto = encontrarProducto(registroVentaEmpleado.nombreProducto());

        double totalAPagar = producto.getPrecio() * registroVentaEmpleado.cantidad();


        if(cliente.isPresent()){ // Verifica si el Optional contiene un valor
            venta.setCliente(cliente.get()); // Si contiene un valor, asigna el cliente
        } else {
            venta.setCliente(null); // Si no contiene un valor, asigna null
        }



        if(registroVentaEmpleado.dinero() < totalAPagar){
            throw  new Exception("Dinero insuficiente");
        }
        double dineroADevolver = totalAPagar - registroVentaEmpleado.dinero();
        if (producto == null){
            throw new Exception("Error con el producto inexsistente");
        }

        venta.setEmpleado(empleado.get());

        venta.setFechaVenta(registroVentaEmpleado.fechaVenta());
        venta.setHoraDeVenta(registroVentaEmpleado.horaDeVenta());
        venta.setCantidad(registroVentaEmpleado.cantidad());
        venta.setProducto(producto);

        if(registroVentaEmpleado.cantidad() > producto.getCantidad()){
            throw new Exception("Cantidad inadecuada");
        }

        int nuevaCantidad = producto.getCantidad() - registroVentaEmpleado.cantidad();
        producto.setCantidad(nuevaCantidad);

        venta.setPrecioUnitario(registroVentaEmpleado.precioUnitario());

        VentaEmpleado ventaNueva = ventaRepo.save(venta);
        productoRepo.save(producto);

        if(cliente.isPresent()){
            emailServicio.enviarCorreo(new EmailDTO(
                    cliente.get().getCorreo(),
                    "Se ha registrado la compra con éxito",
                    "La compra del producto " + registroVentaEmpleado.nombreProducto() + " ha sido un" +
                            " exito, gracias por comprar en la Ventanilla Gimli. La cantidad de producto comprado fue de: " + registroVentaEmpleado.cantidad() +  "." +
                            " Sus devueltas son: " + (dineroADevolver)*(-1) + "$"
            ));
        }

        return ventaNueva.getCodigo();
    }

    public Producto encontrarProducto(String nombre) throws Exception {

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

    @Override
    public int registrarProducto(RegistroProductoDTO registroProductoDTO) throws Exception {


        Producto producto = new Producto();

        producto.setCategoria(registroProductoDTO.categoria());
        producto.setSubcategoria(registroProductoDTO.subcategoria());


        //producto.setNombre(registroProductoDTO.nombre());
        producto.setProveedor(registroProductoDTO.proveedor());
        producto.setDescripcion(registroProductoDTO.descripcion());
        producto.setPrecio(registroProductoDTO.precio());
        producto.setCantidad(registroProductoDTO.cantidad());

        if(producto.getCategoria().equals(Categoria.ALCOHOL)){

            // Verificar si el nombre ya existe en la base de datos
            Producto productoExistente = productoRepo.findByNombresAlcohol(registroProductoDTO.nombre());
            if (productoExistente != null) {
                throw new Exception("Esa bebida ya ha sido agregada con anterioridad");
            }

            // Verificar si el nombre ya está en la lista actual de nombresAlcohol
            if (producto.getNombresAlcohol().contains(registroProductoDTO.nombre())) {
                throw new Exception("Esa bebida ya ha sido agregada con anterioridad");
            }

           /* for (String nombre : producto.getNombresAlcohol()) {
                if (nombre.equals(registroProductoDTO.nombre())) {
                    throw new Exception("Esa bebida ya ha sido agregada con anterioridad");
                }

                if(productoRepo.findByNombresAlcohol(registroProductoDTO.nombre())){
                    throw new Exception("Esa bebida ya ha sido agregada con anterioridad");
                }
            }*/

            // producto.setNombreAlcoholList(nombreAlcoholList);

            producto.getNombresAlcohol().add(registroProductoDTO.nombre());
        } else if (producto.getCategoria().equals(Categoria.DULCES)) {

            Producto productoExistente = productoRepo.findByNombresDulces(registroProductoDTO.nombre());

            if(productoExistente != null){
                throw new Exception("Ese dulce ya ha sido agregado con anterioridad");
            }
            if(producto.getNombresDulces().contains(registroProductoDTO.nombre())){
                throw new Exception("Ese dulce ya ha sido agregado con anterioridad");
            }

            producto.getNombresDulces().add(registroProductoDTO.nombre());

        }else{

            Producto productoExistente = productoRepo.findByNombresGaseosas(registroProductoDTO.nombre());

            if(productoExistente != null){
                throw new Exception("Esa gaseosa ya ha sido agregada con anterioridad");
            }
            if(producto.getNombresDulces().contains(registroProductoDTO.nombre())){
                throw new Exception("Esa gaseosa ya ha sido agregada con anterioridad");
            }

            producto.getNombresGaseosas().add(registroProductoDTO.nombre());
        }

        Producto productoNuevo = productoRepo.save(producto);


        return productoNuevo.getCodigo();
    }


    // --------------------- Borrar -------------------------------------------
    @Override
    public int agregarProducto(AgregarProductoDTO agregarProductoDTO) {


        if(agregarProductoDTO.categoria().equals(Categoria.ALCOHOL)){

            Producto productoEncontrado = productoRepo.findByNombresAlcohol(agregarProductoDTO.nombre());

            int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
            productoEncontrado.setCantidad(nuevaCantidad);

            // Guardar el producto actualizado en la base de datos
            productoRepo.save(productoEncontrado);

        }else if (agregarProductoDTO.categoria().equals(Categoria.DULCES)) {

            Producto productoEncontrado = productoRepo.findByNombresDulces(agregarProductoDTO.nombre());

            int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
            productoEncontrado.setCantidad(nuevaCantidad);

            productoRepo.save(productoEncontrado);

        }else {

            Producto productoEncontrado = productoRepo.findByNombresGaseosas(agregarProductoDTO.nombre());

            int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
            productoEncontrado.setCantidad(nuevaCantidad);

            productoRepo.save(productoEncontrado);
        }

        return 0;
    }

}
