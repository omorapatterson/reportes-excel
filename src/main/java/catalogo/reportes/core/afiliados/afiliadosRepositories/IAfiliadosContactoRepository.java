package catalogo.reportes.core.afiliados.afiliadosRepositories;

import common.rondanet.clasico.core.afiliados.models.Contacto;
import common.rondanet.clasico.core.afiliados.models.ContactoId;
import common.rondanet.clasico.core.afiliados.models.Empresa;
import common.rondanet.clasico.core.afiliados.models.EmpresaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IAfiliadosContactoRepository extends JpaRepository<Contacto, ContactoId> {

    @Transactional
    @Query(value = "SELECT * FROM CTS WHERE EMPRESA = ?1", nativeQuery = true)
    List<Contacto> obtenerContactosEmpresa(String codigoInterno);

}