package co.ventanilla_gimli.repositorios;

import ch.qos.logback.core.net.server.Client;
import co.ventanilla_gimli.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ClienteRepo extends JpaRepository<Cliente, Integer> {

    //Cliente findClienteById(int codigo);

    Cliente findClienteByCorreo(String correo);

    Cliente findByCorreo(String correo);

}
