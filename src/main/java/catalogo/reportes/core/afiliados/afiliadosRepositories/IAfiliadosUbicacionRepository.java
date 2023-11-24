package catalogo.reportes.core.afiliados.afiliadosRepositories;

import common.rondanet.clasico.core.afiliados.models.Ubicacion;
import common.rondanet.clasico.core.afiliados.models.UbicacionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface IAfiliadosUbicacionRepository extends JpaRepository<Ubicacion, UbicacionId> {

    @Transactional
    @Query(value = "SELECT * FROM UBI u join EMP e on u.empresa = e.empresa where e.fbaja is null and u.fbaja is null and e.ruc != '' and u.falta > ?1 rows ?2 to ?3 ", nativeQuery = true)
    List<Ubicacion> findAll(Date fecha, int limit, int offet);

    @Transactional
    @Query(value = "SELECT u.EMPRESA FROM UBI u WHERE u.CODIGO = ?1 AND u.FBAJA IS NULL", nativeQuery = true)
    String obtenerCodigoEmpresa(String gln);

    @Transactional
    @Query(value = "SELECT u.EMPRESA FROM UBI u WHERE u.CODIGO = ?1 AND u.FBAJA IS NULL AND ", nativeQuery = true)
    String obtenerGln(String gln);

    @Transactional
    @Query(value = "SELECT * FROM UBI u WHERE u.EMPRESA = ?1 AND u.FBAJA IS NULL", nativeQuery = true)
    List<Ubicacion> obtenerUbicaciones(String codigoInterno);

    @Transactional
    @Query(value = "SELECT * FROM UBI WHERE CODIGO LIKE '7730740%'", nativeQuery = true)
    List<Ubicacion> obtenerUbicacionesPorGln();

    @Transactional
    @Query(value = "SELECT count(*) FROM EMP e AND e.falta > ?1", nativeQuery = true)
    int getTotal(Date fecha);
}