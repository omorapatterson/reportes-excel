package catalogo.reportes.core.catalogoViejo.catalogoRepositories;

import common.rondanet.clasico.core.catalogo.models.Cliente;
import common.rondanet.clasico.core.catalogo.models.ClienteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ICatalogoViejoClientesRepository extends JpaRepository<Cliente, ClienteId> {

    @Transactional
    @Query(value = "select * from CLI c", nativeQuery = true)
    List<Cliente> findAll();

    @Transactional
    @Query(value = "select * from CLI c where GLN = ?1", nativeQuery = true)
    List<Cliente> findAllByGln(String gln);

    @Transactional
    @Query(value = "select * from CLI c where GLN = ?1 AND GLN2 = ?2", nativeQuery = true)
    Cliente findAllByGlnAndGln2(String gln, String gln2);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CLI c WHERE GLN = ?1", nativeQuery = true)
    void deleteAllByGln(String gln);
}