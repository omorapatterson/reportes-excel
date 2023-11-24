package catalogo.reportes.core.catalogo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import common.rondanet.catalogo.core.entity.Param;

import java.util.List;
import java.util.Optional;

public interface IParamRepository extends MongoRepository<Param, String> {

    public Param findFirstByNombre(String nombre);

    Optional<Param> findByNombreAndEliminadoIsFalse(String propiedad);

    List<Param> findAllByEliminadoIsFalse();
}
