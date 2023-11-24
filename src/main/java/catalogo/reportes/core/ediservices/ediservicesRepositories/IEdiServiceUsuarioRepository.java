package catalogo.reportes.core.ediservices.ediservicesRepositories;

import common.rondanet.clasico.core.ediservices.models.Usuario;
import common.rondanet.clasico.core.ediservices.models.UsuarioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface IEdiServiceUsuarioRepository extends JpaRepository<Usuario, UsuarioId> {

    @Transactional
    @Query(value = "SELECT * FROM USU WHERE EMPRESA = ?1 AND CONTRASENA IS NOT NULL AND CONTRASENA <> ''", nativeQuery = true)
    List<Usuario> obtenerTodosLosUsuarioEmpresa(String codigoInternoEmpresa);

}