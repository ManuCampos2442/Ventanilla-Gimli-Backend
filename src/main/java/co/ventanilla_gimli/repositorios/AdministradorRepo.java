package co.ventanilla_gimli.repositorios;

import co.ventanilla_gimli.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministradorRepo extends JpaRepository<Administrador, Integer> {
    Administrador findByCorreo(String correo);
}
