package catalogo.reportes.core.catalogo.repository;

import common.rondanet.catalogo.core.entity.Empresa;
import org.springframework.data.mongodb.repository.MongoRepository;
import common.rondanet.catalogo.core.entity.Categoria;

import java.util.List;

public interface ICategoriaRepository extends MongoRepository<Categoria, String> {
    public Categoria findFirstByOldId(long oldId);
    public List<Categoria> findAllByEmpresa(Empresa empresa);
    public List<Categoria> findAllBySempresa(String sidEmpresa);
}
