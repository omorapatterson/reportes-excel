package catalogo.reportes.core.catalogoViejo.catalogoRepositories;

import common.rondanet.clasico.core.catalogo.models.ActualizarVisibilidad;
import common.rondanet.clasico.core.catalogo.models.ActualizarVisibilidadId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface IActualizarVisibilidadRepository extends JpaRepository<ActualizarVisibilidad, ActualizarVisibilidadId> {

    @Transactional
    @Query(value = "select GLN, GRUPO from vis Group by GRUPO, GLN rows ?1 to ?2", nativeQuery = true)
    List<ActualizarVisibilidad> findAllGroupByGrupoAndGln(int limit, int offet);

}