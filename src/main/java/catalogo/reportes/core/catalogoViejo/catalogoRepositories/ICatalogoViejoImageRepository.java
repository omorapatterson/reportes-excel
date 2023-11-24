package catalogo.reportes.core.catalogoViejo.catalogoRepositories;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import common.rondanet.clasico.core.catalogo.models.Image;
import common.rondanet.clasico.core.catalogo.models.ImageId;

import javax.transaction.Transactional;

public interface ICatalogoViejoImageRepository extends JpaRepository<Image, ImageId> {

    @Transactional
    @Query(value = "SELECT * FROM IMG u WHERE u.gln = ?1 AND u.fecha_actualiz > ?2", nativeQuery = true)
    List<Image> findImageForUpdate(long gln, Date fecha);

    @Transactional
    @Query(value = "select * from IMG WHERE gln = ?1 and fecha_actualiz > ?2", nativeQuery = true)
    List<Image> findAllByGln(String gln, Date fecha);

    @Transactional
    @Query(value = "select * from IMG WHERE gln = ?1 and fecha_actualiz > ?2 rows ?3 to ?4", nativeQuery = true)
    List<Image> findAllByGln(String gln, Date fecha, int limit, int ofsset);

    @Transactional
    @Query(value = "select * from IMG WHERE gln = ?1", nativeQuery = true)
    List<Image> findAllImagenesByGln(String gln);

    @Transactional
    @Query(value = "select * from IMG WHERE gln = ?1 rows ?2 to ?3", nativeQuery = true)
    List<Image> findAllImagenesByGln(String gln, int limit, int ofsset);

    @Transactional
    @Query(value = "select gln from IMG i WHERE fecha_actualiz > ?1 group by gln", nativeQuery = true)
    List<BigDecimal> findAllGroupByGln(Date fecha);

    @Transactional
    @Query(value = "select gln from IMG i group by gln", nativeQuery = true)
    List<BigDecimal> findAllImagenesGroupByGln();

    @Modifying
    @Transactional
    @Query(value = "DELETE from IMG i where GLN = ?1 and CPP = ?2", nativeQuery = true)
    void deleteAllByGlnAndCpp(String gln, String gln2);

    @Modifying
    @Transactional
    @Query(value = "DELETE from IMG i where GLN = ?1", nativeQuery = true)
    void deleteAllByGln(String gln);
}