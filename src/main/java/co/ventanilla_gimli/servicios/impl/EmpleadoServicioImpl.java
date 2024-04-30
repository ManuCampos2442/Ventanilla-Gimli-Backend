package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.AgregarProductoDTO;
import co.ventanilla_gimli.dto.ClienteDTO.DetalleVentaEmpleadoDTO;
import co.ventanilla_gimli.dto.ItemRegistroProductoDTO;
import co.ventanilla_gimli.dto.DetalleRegistroProductoDTO;
import co.ventanilla_gimli.dto.ItemVentaEmpleadoDTO;
import co.ventanilla_gimli.model.*;
import co.ventanilla_gimli.repositorios.*;
import co.ventanilla_gimli.servicios.interfaces.EmpleadoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpleadoServicioImpl implements EmpleadoServicio {

    private final ClienteRepo clienteRepo;
    private final ProductoRepo productoRepo;
    private final RegistroProductosRepo registroProductosRepo;
    private final EmpleadoRepo empleadoRepo;
    private final AdministradorRepo administradorRepo;
    private final VentaEmpleadoRepo ventaEmpleadoRepo;

    @Override
    public int encontrarClientePorCorreo(String correo) throws Exception {
        Cliente clienteEncontrado = clienteRepo.findClienteByCorreo(correo);
        if (clienteEncontrado != null) {
            return clienteEncontrado.getCodigo();
        } else {
            throw new Exception("No se encontró ningún cliente con el correo proporcionado");
        }
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

            Empleado empleado = new Empleado();
            empleado.setCodigo(codigoEmpleado);


            RegistroProducto r = new RegistroProducto();
            r.setProducto(producto);
            r.setEmpleado(empleado);
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
        VentaEmpleado venta = ventaEncontrada.get();

        String nombreCliente = (venta.getCliente() != null) ? venta.getCliente().getNombre() : "Cliente casual";
        String nombreProducto = encontrarProductoPorCodigo(venta.getProducto().getCodigo());

        return new DetalleVentaEmpleadoDTO(
                venta.getCodigo(),
                venta.getFechaVenta(),
                venta.getHoraDeVenta(),
                venta.getEmpleado().getNombre(),
                venta.getEmpleado().getCodigo(),
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

}
