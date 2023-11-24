package catalogo.reportes.core.catalogoViejo.catalogoRepositories;

import common.rondanet.clasico.core.catalogo.models.Producto;
import common.rondanet.clasico.core.catalogo.models.ProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ICatalogoViejoProductoRepository extends JpaRepository<Producto, ProductoId> {

    @Query(value = "select * from pro p WHERE p.GLN = ?1 and p.CPP = ?2", nativeQuery = true)
    Producto findByGlnAndCpp(String gln, String cppProducto);

    @Transactional
    @Query(value = "SELECT * FROM pro p WHERE GLN = ?1", nativeQuery = true)
    List<Producto> findAllByGln(String gln);

    @Transactional
    @Query(value = "SELECT * FROM pro p WHERE GLN = ?1 AND SUBLINEA = ?2", nativeQuery = true)
    List<Producto> findAllByGlnAndSubLinea(String gln, String subLinea);

    @Transactional
    @Query(value = "SELECT count(*) FROM pro p WHERE GLN = ?1", nativeQuery = true)
    long getTotal(String gln);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM pro p WHERE GLN = ?1", nativeQuery = true)
    void deleteAllByGln(String gln);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM pro p WHERE GLN = ?1 AND CPP = ?2", nativeQuery = true)
    void deleteAllByGlnAndCpp(String gln, String cpp);

}