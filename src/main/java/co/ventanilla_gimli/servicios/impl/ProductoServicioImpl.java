package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.RegistroProductoDTO;
import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Producto;
import co.ventanilla_gimli.repositorios.ProductoRepo;
import co.ventanilla_gimli.servicios.interfaces.ProductoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoServicioImpl implements ProductoServicio {

    private final ProductoRepo productoRepo;

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

            for (String nombre : producto.getNombresAlcohol()) {
                if (nombre.equals(registroProductoDTO.nombre())) {
                    throw new Exception("Esa bebida ya ha sido agregada con anterioridad");
                }
            }

           // producto.setNombreAlcoholList(nombreAlcoholList);

            producto.getNombresAlcohol().add(registroProductoDTO.nombre());
        }else if (producto.getCategoria().equals(Categoria.DULCES)){
            for (String nombre : producto.getNombresDulces()){
                if(nombre.equals(registroProductoDTO.nombre())){
                    throw new Exception("Ese dulce ya ha sido agregada con anterioridad");
                }
            }
        }else{
            for (String nombre : producto.getNombresDulces()){
                if(nombre.equals(registroProductoDTO.nombre())){
                    throw new Exception("Esa gaseosa ya ha sido agregada con anterioridad");
                }
            }
        }

        Producto productoNuevo = productoRepo.save(producto);


        return productoNuevo.getCodigo();
    }


}
