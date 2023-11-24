package catalogo.reportes.core.afiliados.afiliadosRepositories;

import common.rondanet.clasico.core.afiliados.models.Producto;
import common.rondanet.clasico.core.afiliados.models.ProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
public interface IAfiliadosProductoRepository extends JpaRepository<Producto, ProductoId> {

    @Transactional
    @Query(value = "SELECT * FROM pro p rows ?1 to ?2", nativeQuery = true)
    List<Producto> findAll(int limit, int offet);

    @Transactional
    @Query(value = "SELECT * FROM pro p WHERE EMPRESA = ?1 rows ?2 to ?3", nativeQuery = true)
    List<Producto> findAllProductosByEmpresa(String empresa, int limit, int offet);

    @Transactional
    @Query(value = "SELECT * FROM pro p WHERE CODIGO = ?1 rows 1 to 2", nativeQuery = true)
    Producto findByGtin(String gtin);

    @Transactional
    @Query(value = "SELECT * FROM pro p WHERE CODIGO IN ?1", nativeQuery = true)
    List<Producto> findAllByGtin(List<String> productosGtin);

    @Transactional
    @Query(value = "SELECT * FROM pro p WHERE EMPRESA = ?1 AND CHAR_LENGTH (CODIGO) = ?2 AND CODIGO LIKE '773%' rows ?3 to ?4", nativeQuery = true)
    List<Producto> findAllProductosByEmpresaConFecha(String empresa, long gtinLongitud, int limit, int offet);

    @Transactional
    @Query(value = "SELECT * FROM pro p WHERE EMPRESA = ?1 AND CHAR_LENGTH (CODIGO) = ?2 AND CODIGO LIKE '773%' AND EXISTS(SELECT * FROM pro e WHERE e.CODIGOBASE = p.codigo AND CHAR_LENGTH (e.CODIGO) = 14) rows ?3 to ?4", nativeQuery = true)
    List<Producto> findAllProductosByEmpresaConFechaAndEmpaque(String empresa, long gtinLongitud, int limit, int offet);

    @Transactional
    @Query(value = "SELECT * FROM pro p WHERE EMPRESA = ?1 AND CHAR_LENGTH (CODIGO) = ?2 AND CODIGO LIKE '773%' AND FALTA >= ?3 rows ?4 to ?5", nativeQuery = true)
    List<Producto> findAllProductosByEmpresaPorFecha(String empresa, long gtinLongitud, Date fecha, int limit, int offet);

    @Transactional
    @Query(value = "SELECT * FROM pro p WHERE EMPRESA = ?1 AND CHAR_LENGTH (CODIGO) = ?2 AND CODIGO LIKE '773%' AND FALTA >= ?3 AND EXISTS(SELECT * FROM pro e WHERE e.CODIGOBASE = p.codigo AND CHAR_LENGTH (e.CODIGO) = 14) rows ?4 to ?5", nativeQuery = true)
    List<Producto> findAllProductosByEmpresaPorFechaAndEmpaque(String empresa, long gtinLongitud, Date fecha, int limit, int offet);

    @Transactional
    @Query(value = "SELECT count(*) FROM pro p WHERE EMPRESA = ?1 AND CHAR_LENGTH (CODIGO) = ?2 AND CODIGO LIKE '773%'", nativeQuery = true)
    Integer countAllProductosByEmpresaConFechas(String empresa, long gtinLongitud);

    @Transactional
    @Query(value = "SELECT count(*) FROM pro p WHERE EMPRESA = ?1 AND CHAR_LENGTH (CODIGO) = ?2 AND CODIGO LIKE '773%' AND EXISTS(SELECT * FROM pro e WHERE e.CODIGOBASE = p.codigo AND CHAR_LENGTH (e.CODIGO) = 14)", nativeQuery = true)
    Integer countAllProductosByEmpresaConFechasAndEmpaque(String empresa, long gtinLongitud);

    @Transactional
    @Query(value = "SELECT count(*) FROM pro p WHERE EMPRESA = ?1 AND CHAR_LENGTH (CODIGO) = ?2 AND CODIGO LIKE '773%' AND FALTA >= ?3", nativeQuery = true)
    Integer countAllProductosByEmpresaPorFecha(String empresa, long gtinLongitud, Date fecha);

    @Transactional
    @Query(value = "SELECT count(*) FROM pro p WHERE EMPRESA = ?1 AND CHAR_LENGTH (CODIGO) = ?2 AND CODIGO LIKE '773%' AND FALTA >= ?3 AND EXISTS(SELECT * FROM pro e WHERE e.CODIGOBASE = p.codigo AND CHAR_LENGTH (e.CODIGO) = 14)", nativeQuery = true)
    Integer countAllProductosByEmpresaPorFechaAndEmpaque(String empresa, long gtinLongitud,  Date fecha);

    @Transactional
    @Query(value = "SELECT * FROM pro p WHERE CHAR_LENGTH (CODIGO) = 14 AND CODIGOBASE = ?1", nativeQuery = true)
    List<Producto> findEmpaqueProducto(String gtin13);

    @Transactional
    @Query(value = "SELECT EMPRESA FROM PRO WHERE CHAR_LENGTH (CODIGO) = ?1 AND CODIGO LIKE '773%' GROUP BY EMPRESA", nativeQuery = true)
    List<String> findGroupByEmpresa(long gtinLongitud);

    @Transactional
    @Query(value = "SELECT EMPRESA  FROM Emp e WHERE (TIPOCUOTA <> '0' OR (CUOTAMIN <> 0 AND PRECIOCA <> 0)) OR RUC IN ?1", nativeQuery = true)
    List<String> findGroupByEmpresaParaActivate(List<String> excepcionesEmpresa);
}