package co.ventanilla_gimli.repositorios;

import co.ventanilla_gimli.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepo extends JpaRepository<Cuenta, Integer> {

    Optional<Cuenta> findByCorreo(String correo);
}
