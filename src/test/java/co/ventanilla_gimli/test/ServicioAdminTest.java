package co.ventanilla_gimli.test;
/*

import co.ventanilla_gimli.dto.AdministradorDTO.ModificarEmpleadoAdminDTO;
import co.ventanilla_gimli.dto.ClienteDTO.DetalleVentaEmpleadoDTO;
import co.ventanilla_gimli.dto.ClienteDTO.ModificarClienteDTO;
import co.ventanilla_gimli.dto.FiltroBusquedaDTO;
import co.ventanilla_gimli.dto.ItemProductoDTO;
import co.ventanilla_gimli.dto.ItemVentaEmpleadoDTO;
import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.servicios.interfaces.AdministradorServicio;
import co.ventanilla_gimli.servicios.interfaces.ClienteServicio;
import co.ventanilla_gimli.servicios.interfaces.EmailServicio;
import co.ventanilla_gimli.servicios.interfaces.EmpleadoServicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class ServicioAdminTest {

    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    private EmpleadoServicio empleadoServicio;
    @Autowired
    private EmailServicio emailServicio;
    @Autowired
    private AdministradorServicio administradorServicio;

    @Test
    public void listarCedulas() throws Exception {
        // Arrange: Preparar datos de prueba
        List<String> cedulasEsperadas = Arrays.asList("cedula1", "cedula2", "cedula3");
        when(administradorServicio.obtenerEmpleados()).thenReturn(cedulasEsperadas);

        // Act: Llamar al método a probar
        List<String> cedulasObtenidas = administradorServicio.obtenerEmpleados();

        // Assert: Verificar el resultado
        System.out.println("\nCedulas obtenidas: " + cedulasObtenidas);
        Assertions.assertNotEquals(0, cedulasObtenidas.size(), "La lista de cédulas no debería estar vacía");
    }

    @Test
    public  void modificarDatos() throws Exception {

        ModificarEmpleadoAdminDTO clienteDTO = new ModificarEmpleadoAdminDTO(
                "Mari",
                "23232333",
                "109488123",
                "marianauv@gmail.com",
                "miperri@gmail.com",
                "1234"
        );

        String nuevo =  administradorServicio.modificarEmpleado(clienteDTO);

        Assertions.assertNotEquals(0, nuevo);
    }

    @Test
    public void listaVentasEmpleados(){
        List<ItemVentaEmpleadoDTO> ventas = empleadoServicio.listaVentasEmpleados();

        ventas.forEach(System.out::println);

        Assertions.assertEquals(10, +ventas.size());
    }

    @Test
    public void detallesVenta() throws Exception {

        DetalleVentaEmpleadoDTO nuevo =  empleadoServicio.verDetalleVentaEmpleado(8);

        System.out.println(nuevo.toString());

        Assertions.assertNotEquals(0, nuevo);
    }
}
*/
