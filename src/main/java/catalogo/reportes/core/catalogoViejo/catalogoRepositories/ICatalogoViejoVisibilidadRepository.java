package catalogo.reportes.core.catalogoViejo.catalogoRepositories;

import common.rondanet.clasico.core.catalogo.models.VisibilidadProducto;
import common.rondanet.clasico.core.catalogo.models.VisibilidadProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

public interface ICatalogoViejoVisibilidadRepository extends JpaRepository<VisibilidadProducto, VisibilidadProductoId> {

    @Transactional
    @Query(value = "select * from VIS order by GLN rows ?1 to ?2", nativeQuery = true)
    List<VisibilidadProducto> findAll(int limit, int offet);

    @Transactional
    @Query(value = "select * from VIS where GLN = ?1 ", nativeQuery = true)
    List<VisibilidadProducto> findAllByGln(String gln);

    @Transactional
    @Query(value = "select * from VIS where GLN = ?1 rows ?2 to ?3", nativeQuery = true)
    List<VisibilidadProducto> findAllByGln(String gln, int limit, int offet);

    @Transactional
    @Query(value = "select count(*) from VIS where GLN = ?1 ", nativeQuery = true)
    int countAllProductosPrivadosByGln(String gln);

    @Transactional
    @Query(value = "select CPP from PRO where GLN = ?1 AND VISIBILIDAD = 'T'", nativeQuery = true)
    List<String> findAllProductosPublicoByGln(String gln);

    @Transactional
    @Query(value = "select CPP from PRO where GLN = ?1 AND VISIBILIDAD = 'T' rows ?2 to ?3", nativeQuery = true)
    List<String> findAllProductosPublicoByGln(String gln, int limit, int offet);

    @Transactional
    @Query(value = "select count(*) from PRO where GLN = ?1 AND VISIBILIDAD = 'T'", nativeQuery = true)
    int countAllProductosPublicosByGln(String gln);

    @Transactional
    @Query(value = "select GLN from VIS group By GLN rows ?1 to ?2", nativeQuery = true)
    List<String> findAllGln(int limit, int offet);

    @Transactional
    @Query(value = "select GLN from VIS group By GLN", nativeQuery = true)
    List<BigDecimal> findAllGroupByGln();

    @Transactional
    @Query(value = "select GLN from VIS where gln not in(?1) group By GLN", nativeQuery = true)
    List<BigDecimal> findAllGroupByGln(List<String> glns);

    @Transactional
    @Query(value = "select GLN, GRUPO from VIS Group by GRUPO, GLN rows ?1 to ?2", nativeQuery = true)
    List<VisibilidadProducto> findAllGroupByGrupoAndGln(int limit, int offet);

    @Transactional
    @Query(value = "SELECT count(*) FROM VIS", nativeQuery = true)
    int countAllProductosPrivadosByGln();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM VIS v WHERE GLN = ?1", nativeQuery = true)
    void deleteAllByGln(String gln);

    @Transactional
    @Query(value = "select * from VIS v where GLN = ?1 and CPP = ?2 and grupo = ?3", nativeQuery = true)
    VisibilidadProducto findAllByGlnAndCppAndGrupo(String gln, String gln2, String grupo);

    @Modifying
    @Transactional
    @Query(value = "DELETE from VIS v where GLN = ?1 and CPP = ?2 and GRUPO = ?3", nativeQuery = true)
    void deleteAllByGlnAndCppAndGrupo(String gln, String gln2, String grupo);

    @Modifying
    @Transactional
    @Query(value = "DELETE from VIS v where GLN = ?1 and CPP = ?2", nativeQuery = true)
    void deleteAllByGlnAndCpp(String gln, String gln2);

    @Query(value = "SELECT count(*) FROM  LISTAR_PRODUCTOS_PARA_OC(?1, ?2, 'S', '')", nativeQuery = true)
    Integer getTotalDeProductosVisiblesOrdenCompra(String proveedorGln, String clienteGln);
}