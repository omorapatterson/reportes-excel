package catalogo.reportes.core.catalogoViejo.catalogoRepositories;

import common.rondanet.clasico.core.catalogo.models.Producto;
import common.rondanet.clasico.core.catalogo.models.ProductoGtin;
import common.rondanet.clasico.core.catalogo.models.ProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ICatalogoViejoProductoGtinRepository extends JpaRepository<ProductoGtin, ProductoId> {

    @Transactional
    @Query(value = "select p.*, g.GTIN13, g.GTIN14, g.UNIDADES, g.EMPAQUES, g.CAMADAS, g.PAIS_ID, g.LINEAPLA from pro p join GTI g on g.GLN = p.GLN and g.cpp = p.cpp WHERE p.FECHA_ACTUALIZ > ?1 rows ?2 to ?3", nativeQuery = true)
    List<ProductoGtin> findAll(Date fecha, int limit, int offet);

    @Transactional
    @Query(value = "select p.*, g.GTIN13, g.GTIN14, g.UNIDADES, g.EMPAQUES, g.CAMADAS, g.PAIS_ID, g.LINEAPLA from pro p join GTI g on g.GLN = p.GLN and g.cpp = p.cpp WHERE p.GLN = ?1 and p.FECHA_ACTUALIZ > ?2", nativeQuery = true)
    List<ProductoGtin> findAllByGln(String gln, Date fecha);

    @Transactional
    @Query(value = "select p.*, g.GTIN13, g.GTIN14, g.UNIDADES, g.EMPAQUES, g.CAMADAS, g.PAIS_ID, g.LINEAPLA from pro p join GTI g on g.GLN = p.GLN and g.cpp = p.cpp WHERE p.GLN = ?1 and p.FECHA_ACTUALIZ > ?2  rows ?3 to ?4", nativeQuery = true)
    List<ProductoGtin> findAllByGln(String gln, Date fecha, int limit, int offet);

    @Transactional
    @Query(value = "select p.*, g.GTIN13, g.GTIN14, g.UNIDADES, g.EMPAQUES, g.CAMADAS, g.PAIS_ID, g.LINEAPLA from pro p join GTI g on g.GLN = p.GLN and g.cpp = p.cpp WHERE p.GLN = ?1 rows ?2 to ?3", nativeQuery = true)
    List<ProductoGtin> findAllByGln(String gln, int limit, int offet);

    @Transactional
    @Query(value = "select * from pro p WHERE p.GLN = ?1 and p.CPP = ?2", nativeQuery = true)
    Producto findAllByGlnAndCpp(String gln, String cppProducto);

    @Transactional
    @Query(value = "select p.gln from pro p group by p.gln", nativeQuery = true)
    List<BigDecimal> findAllGroupByGln();

    @Query(value = "SELECT count(*) FROM PRO p JOIN GTI g ON p.CPP = g.CPP AND p.GLN = g.GLN WHERE p.GLN = ?1 AND g.GTIN13 IS NOT NULL AND g.GTIN13 > 0", nativeQuery = true)
    int totalDeProductosConGTIN13(String gln);

    @Query(value = "SELECT count(*) FROM PRO p JOIN GTI g ON p.CPP = g.CPP AND p.GLN = g.GLN WHERE p.GLN = ?1 AND g.GTIN14 IS NOT NULL AND g.GTIN14 > 0", nativeQuery = true)
    int totalDeProductosConGTIN14(String gln);

    @Query(value = "SELECT p.FECHA_ACTUALIZ FROM PRO p WHERE p.GLN = ?1 ORDER BY FECHA_ACTUALIZ DESC ROWS 1", nativeQuery = true)
    Date ultimaFechaDeActualizacion(String gln);

    @Transactional
    @Query(value = "SELECT count(*) FROM PRO p AND p.FECHA_ACTUALIZ > ?1", nativeQuery = true)
    int getTotal(Date fecha);

    @Query(value = "SELECT count(*) FROM PRO WHERE \n" +
            "(PRO.GLN = ?1) AND ((PRO.FECHA_SUSPEND1 IS NULL) OR (PRO.FECHA_SUSPEND1 >= CURRENT_TIMESTAMP - 25) OR (PRO.FECHA_SUSPEND2 IS NOT NULL)) AND ((PRO.VISIBILIDAD = 'T' AND EXISTS (SELECT * FROM GRU WHERE GRU.GLN = PRO.GLN AND GRU.GLN2 IN ?2)) OR\n" +
            "                    (EXISTS (SELECT VISIBLE FROM VIS WHERE VIS.GLN = PRO.GLN AND VIS.CPP = PRO.CPP AND\n" +
            "                          VIS.GRUPO IN (SELECT GRUPO FROM GRU\n" +
            "                                      WHERE GRU.GLN = PRO.GLN AND\n" +
            "                                            GRU.GLN2 IN ?2))))", nativeQuery = true)
    int getTotalProductosVisibles(String gln, List<String> glns);

}