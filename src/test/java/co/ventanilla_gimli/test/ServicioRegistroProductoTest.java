package co.ventanilla_gimli.test;

import co.ventanilla_gimli.dto.AgregarProductoDTO;
import co.ventanilla_gimli.dto.ItemRegistroProductoDTO;
import co.ventanilla_gimli.dto.RegistroAgregacionProductoDTO;
import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.servicios.interfaces.EmpleadoServicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ServicioRegistroProductoTest {

    @Autowired
    private EmpleadoServicio empleadoServicio;

    @Test
    private void agregarProducto(){

        AgregarProductoDTO agregarProductoDTO = new AgregarProductoDTO(
                Categoria.DULCES,
                2,
                "Cheetos Flaming Hot",
                67
        );

    }

    @Test
    public void listarCitasRealizadas() throws Exception {

        List<ItemRegistroProductoDTO> lista = empleadoServicio.listarRegistroProductos();

        lista.forEach(System.out::println);
        //Si en el dataset creamos 2 pacientes, entonces el tamaño de la lista debe ser 2
        Assertions.assertEquals(1, lista.size());
    }
}
