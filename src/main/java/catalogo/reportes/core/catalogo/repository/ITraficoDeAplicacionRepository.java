package catalogo.reportes.core.catalogo.repository;

import common.rondanet.catalogo.core.entity.TraficoDeAplicacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITraficoDeAplicacionRepository extends MongoRepository<TraficoDeAplicacion, String> {
    
}
