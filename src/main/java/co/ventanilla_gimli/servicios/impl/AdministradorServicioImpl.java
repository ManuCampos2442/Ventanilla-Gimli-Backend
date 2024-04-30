package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.AdministradorDTO.ModificarEmpleadoAdminDTO;
import co.ventanilla_gimli.dto.RegistroEmpleadoDTO;
import co.ventanilla_gimli.model.Administrador;
import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Cliente;
import co.ventanilla_gimli.model.Empleado;
import co.ventanilla_gimli.repositorios.*;
import co.ventanilla_gimli.servicios.interfaces.AdministradorServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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


}
