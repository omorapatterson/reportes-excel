package catalogo.reportes.core.catalogoViejo.catalogoRepositories;

import common.rondanet.clasico.core.catalogo.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ICatalogoViejoGtiRepository extends JpaRepository<Gti, GtiId> {

    @Modifying
    @Transactional
    @Query(value = "DELETE from Gti g where GLN = ?1 and CPP = ?2", nativeQuery = true)
    void deleteAllByGlnAndCpp(String gln, String gln2);

    @Modifying
    @Transactional
    @Query(value = "DELETE from Gti g where GLN = ?1", nativeQuery = true)
    void deleteAllByGln(String gln);

}