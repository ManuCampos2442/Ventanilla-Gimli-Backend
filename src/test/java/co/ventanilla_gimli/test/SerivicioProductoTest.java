package co.ventanilla_gimli.test;

import co.ventanilla_gimli.dto.AgregarProductoDTO;
import co.ventanilla_gimli.dto.RegistroProductoDTO;
import co.ventanilla_gimli.dto.RegistroVentaEmpleadoDTO;
import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Subcategoria;
import co.ventanilla_gimli.servicios.impl.ProductoServicioImpl;
import co.ventanilla_gimli.servicios.interfaces.ProductoServicio;
import co.ventanilla_gimli.servicios.interfaces.VentanillaServicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SerivicioProductoTest {

    @Autowired
    private ProductoServicio productoServicio;

    @Autowired
    private VentanillaServicio ventanillaServicio;

    ProductoServicioImpl p;


   /* @Test
    public void registrarProductoTest() throws Exception {

        ArrayList<String> nombreAlcohol = new ArrayList<>();


        RegistroProductoDTO registroProductoDTO = new RegistroProductoDTO(
                Categoria.ALCOHOL,
                Subcategoria.CERVEZA,
                "Tecate",
                "Rica cerveza de las mejores ñam ñam",
                12.45,
                5,
                nombreAlcohol
        );

        //Guardamos el registro usando el método del servicio
        int nuevo = productoServicio.registrarProducto(registroProductoDTO);
        //Comprobamos que sí haya quedado guardado. Si se guardó debe retornar el código (no 0).
        Assertions.assertNotEquals(0, nuevo);
    }*/

    @Test
    public void listarNombresAlcoholes() throws Exception {

        List<String> nombresAlcoholes = ventanillaServicio.listarNombresAlcoholes(Categoria.ALCOHOL);

        nombresAlcoholes.forEach(System.out::println);

        Assertions.assertEquals(2, +nombresAlcoholes.size());
    }

   /* @Test
    public void registrarVentaEmpleado(){

            RegistroVentaEmpleadoDTO registroVentaEmpleado = new RegistroVentaEmpleadoDTO(
                    LocalDate.of(2024, 02, 16),
                    "2:00 PM",
                    1,
                    2500,
                    1,
                    100,
                    67

            );

        int nuevo = ventanillaServicio.registrarVentaEmpleado(registroVentaEmpleado);
        //Comprobamos que sí haya quedado guardado. Si se guardó debe retornar el código (no 0).
        Assertions.assertNotEquals(0, nuevo);
    }*/

    @Test
    public void agregarProducto(){

        AgregarProductoDTO agregarProductoDTO = new AgregarProductoDTO(
                Categoria.DULCES,
                2,
                "Cheetos Flaming Hot",
                67
        );

        int nuevo = ventanillaServicio.agregarProducto(agregarProductoDTO);
        Assertions.assertNotEquals(0, nuevo);
    }

    @Test
    public void realizarVenta() throws Exception {



       // int nuevo = ventanillaServicio.registrarVentaEmpleado(registro);
        //Assertions.assertNotEquals(0, nuevo);

      // System.out.println(p.encontrarProducto("Tecate"));

    }


}
