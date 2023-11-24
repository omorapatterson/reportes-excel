package catalogo.reportes.core.afiliados.afiliadosRepositories;

import common.rondanet.clasico.core.afiliados.models.Cabezal;
import common.rondanet.clasico.core.afiliados.models.Contacto;
import common.rondanet.clasico.core.afiliados.models.ContactoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface IAfiliadosCabezalRepository extends JpaRepository<Cabezal, ContactoId> {

    @Transactional
    @Query(value = "SELECT * FROM CAB WHERE EMPRESA = ?1 AND NOTIFTIPO <> 'P' AND NOTIFTIPO <> 'U'", nativeQuery = true)
    List<Cabezal> obtenerCabezalesEmpresa(String codigoInterno);

}