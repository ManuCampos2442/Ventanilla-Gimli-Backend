package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.LoginDTO;
import co.ventanilla_gimli.dto.TokenDTO.TokenDTO;
import co.ventanilla_gimli.model.Cuenta;
import co.ventanilla_gimli.model.Empleado;
import co.ventanilla_gimli.model.Cliente;
import co.ventanilla_gimli.repositorios.CuentaRepo;
import co.ventanilla_gimli.repositorios.EmpleadoRepo;
import co.ventanilla_gimli.repositorios.ClienteRepo;
import co.ventanilla_gimli.servicios.interfaces.AutenticacionServicio;
import co.ventanilla_gimli.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutenticacionServicioImpl implements AutenticacionServicio {

    private final CuentaRepo cuentaRepo;
    private  final ClienteRepo clienteRepo;
    private  final EmpleadoRepo empleadoRepo;
    private final JWTUtils jwtUtils;
   @Override
    public TokenDTO login(LoginDTO loginDTO) throws Exception {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Optional<Cuenta> cuentaOptional = cuentaRepo.findByCorreo(loginDTO.correo());

       if (cuentaOptional.isEmpty()) {
           throw new Exception("Datos incorrectos, verifique nuevamente");
       }

       Cuenta cuenta = cuentaOptional.get();

       // Verificar el estado de la cuenta
       if (cuenta instanceof Cliente) {
           Cliente cuentaPaciente = clienteRepo.findByCorreo(cuenta.getCorreo());
           if (!cuentaPaciente.isEstado()) {
               throw new Exception("La cuenta ha sido eliminada");
           }
       }

       // Aquí puedes agregar más lógica para otros tipos de cuenta, si es necesario

       // Resto del código para verificar la contraseña y generar el token
       if (!passwordEncoder.matches(loginDTO.password(), cuenta.getPassword())) {
           throw new Exception("La contraseña ingresada es incorrecta");
       }

       return new TokenDTO(crearToken(cuenta));


    }

    private String crearToken(Cuenta cuenta){
        String rol;
        String nombre;
        if( cuenta instanceof Cliente){
            rol = "cliente";
            nombre = ((Cliente) cuenta).getNombre();
        }else if( cuenta instanceof Empleado){
            rol = "empleado";
            nombre = ((Empleado) cuenta).getNombre();
        }else{
            rol = "admin";
            nombre = "Administrador";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("rol", rol);
        map.put("nombre", nombre);
        map.put("id", cuenta.getCodigo());

        return jwtUtils.generarToken(cuenta.getCorreo(), map);
    }


}