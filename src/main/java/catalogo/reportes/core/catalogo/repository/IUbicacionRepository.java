package catalogo.reportes.core.catalogo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import common.rondanet.catalogo.core.entity.Ubicacion;

public interface IUbicacionRepository extends MongoRepository<Ubicacion, String> {
    public Ubicacion findFirstByOldId(long oldId);
}
