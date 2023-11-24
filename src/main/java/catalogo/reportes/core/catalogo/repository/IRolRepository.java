package catalogo.reportes.core.catalogo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import common.rondanet.catalogo.core.entity.Rol;

public interface IRolRepository extends MongoRepository<Rol, String> {
    public Rol findFirstByOldId(long oldId);
}
