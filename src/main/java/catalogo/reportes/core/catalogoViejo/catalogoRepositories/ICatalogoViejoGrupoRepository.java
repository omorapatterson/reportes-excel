package catalogo.reportes.core.catalogoViejo.catalogoRepositories;

import common.rondanet.clasico.core.catalogo.models.Grupo;
import common.rondanet.clasico.core.catalogo.models.GrupoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ICatalogoViejoGrupoRepository extends JpaRepository<Grupo, GrupoId> {

    @Transactional
    @Query(value = "select * from GRU g", nativeQuery = true)
    List<Grupo> findAll();

    @Transactional
    @Query(value = "select * from GRU g where gln = ?1", nativeQuery = true)
    List<Grupo> findAllByGln(String gln);

    @Transactional
    @Query(value = "select * from GRU g where gln = ?1 rows ?2 to ?3", nativeQuery = true)
    List<Grupo> findAllByGln(String gln, int limit, int offet);

    @Transactional
    @Query(value = "select GRUPO from GRU g where gln = ?1 group by GRUPO", nativeQuery = true)
    List<String> findAllByGlnGroupByNombre(String gln);

    @Transactional
    @Query(value = "select * from GRU g where gln = ?1 and gln2 = ?2 and grupo = ?3", nativeQuery = true)
    Grupo findByGlnAndGl2AndNombre(String gln, String gln2, String grupo);

    @Transactional
    @Query(value = "select * from GRU g where gln = ?1 and gln2 = ?2", nativeQuery = true)
    List<Grupo> findAllByGlnAndGl2(String gln, String gln2);

    @Transactional
    @Query(value = "select gln2 from GRU g WHERE GLN = ?1 group by gln2", nativeQuery = true)
    List<BigDecimal> findAllByGlnGroupByGln2(String gln);

    @Transactional
    @Query(value = "select gln from GRU g group by gln", nativeQuery = true)
    List<BigDecimal> findAllGroupByGln();


    @Transactional
    @Query(value = "SELECT count(*) FROM GRU", nativeQuery = true)
    int getTotal(Date fecha);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM GRU g WHERE GLN = ?1", nativeQuery = true)
    void deleteAllByGln(String gln);
}