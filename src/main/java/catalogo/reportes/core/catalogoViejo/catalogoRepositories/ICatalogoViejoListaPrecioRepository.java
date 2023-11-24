package catalogo.reportes.core.catalogoViejo.catalogoRepositories;

import common.rondanet.clasico.core.catalogo.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ICatalogoViejoListaPrecioRepository extends JpaRepository<ListaPrecio, ListaPrecioId> {

    @Transactional
    @Query(value = "select * from lp WHERE VIGENCIA > ?1 and PRBASE > 0 Order by gln rows ?2 to ?3", nativeQuery = true)
    List<ListaPrecio> findAll(Date fecha, int limit, int offet);

    @Transactional
    @Query(value = "select * from lp as lp where not exists ( select * from lp as lp2 where lp.CPP = lp2.cpp and lp.GLN = lp2.GLN and lp.vigencia < lp2.vigencia) Order by gln rows ?1 to ?2", nativeQuery = true)
    List<ListaPrecio> findAllVigencia(int limit, int offet);

    @Transactional
    @Query(value = "select gln from LP lp group by gln", nativeQuery = true)
    List<BigDecimal> findAllGroupByGln();

    @Transactional
    @Query(value = "select target from LP where gln = ?1 group by target", nativeQuery = true)
    List<String> findAllByGlnGroupByTarget(String gln);

    @Transactional
    @Query(value = "SELECT MONEDA FROM LP WHERE GLN = ?1 AND TARGET = ?2 GROUP BY MONEDA", nativeQuery = true)
    List<String> findAllByGlnAndTargetGroupByMoneda(String gln, String target);

    @Transactional
    @Query(value = "SELECT MAX(VIGENCIA) FROM LP WHERE GLN = ?1 AND TARGET = ?2 AND MONEDA = ?3 AND VIGENCIA <= CURRENT_DATE", nativeQuery = true)
    Date findUltimaFechaVigenciaByGlnAndTargetAndMoneda(String gln, String target, String moneda);

    @Transactional
    @Query(value = "SELECT * FROM LP WHERE GLN = ?1 AND TARGET = ?2 AND MONEDA = ?3 AND VIGENCIA = ?4", nativeQuery = true)
    List<ListaPrecio> findAllByGlnAndTargetAndMonedaAndFechaVigencia(String gln, String target, String moneda, Date vigencia);

    @Transactional
    @Query(value = "SELECT count(*) FROM GRU", nativeQuery = true)
    int getTotal(Date fecha);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM LP lp WHERE GLN = ?1 AND TARGET = ?2 AND  VIGENCIA = ?3", nativeQuery = true)
    void deleteAllByGlnAndTarget(String gln, String target, Date fechaVigencia);
}