package catalogo.reportes.core.catalogo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import common.rondanet.catalogo.core.entity.Presentacion;

public interface IPresentacionRepository extends MongoRepository<Presentacion, String> {
    public Presentacion findFirstByNombre(String nombre);
}
