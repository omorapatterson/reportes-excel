package catalogo.reportes.core.pedidos.pedidosRepository;

import catalogo.reportes.core.pedidos.pedidosEntity.DatosAfiliados;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IDatosAfiliadosRepository extends MongoRepository<DatosAfiliados, String> {
    public DatosAfiliados findFirstByOldId(long oldId);
}
