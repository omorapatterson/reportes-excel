package catalogo.reportes.core.catalogo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import common.rondanet.catalogo.core.entity.ProductoAccion;

import java.util.List;

public interface IProductoAccionRepository extends MongoRepository<ProductoAccion, String> {
    public ProductoAccion findFirstByOldId(long oldId);

    public List<ProductoAccion> findAllByIdIn(List<String> accionesId);
    public ProductoAccion findFirstByProveedorIdAndEmpresaIdAndProductoId(String proveedorId, String empresaId, String productoId);

    public List<ProductoAccion> findAllByProveedorIdAndEmpresaId(String proveedorId, String empresaId);
}
