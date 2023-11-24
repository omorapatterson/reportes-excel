package catalogo.reportes.core.catalogo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import common.rondanet.catalogo.core.entity.Baja;

public interface IBajaRepository extends MongoRepository<Baja, String> {
    public Baja findFirstByOldId(long oldId);
}
