package catalogo.reportes.core.catalogo.repository;

import common.rondanet.catalogo.core.entity.ListaDeVentaVisibilidad;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IListaDeVentaVisibilidadRepository extends MongoRepository<ListaDeVentaVisibilidad, String> {
    public ListaDeVentaVisibilidad findFirstByOldId(long oldId);
}
