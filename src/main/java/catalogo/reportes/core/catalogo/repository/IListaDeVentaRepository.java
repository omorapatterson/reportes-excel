package catalogo.reportes.core.catalogo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import common.rondanet.catalogo.core.entity.ListaDeVenta;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IListaDeVentaRepository extends MongoRepository<ListaDeVenta, String> {
    public ListaDeVenta findFirstByOldId(long oldId);
    public List<ListaDeVenta> findAllBySempresaAndSempresasInOrSgruposIn(String proveedor, String empresa,  Set<String> grupos);
    public Optional<ListaDeVenta> findFirstBySubicacionAndNombre(String ubicacion, String nombre);
    public List<ListaDeVenta> findAllBySubicacion(String ubicacion);
    public List<ListaDeVenta> findAllBySempresa(String proveedor);
    public Optional<ListaDeVenta> findBySubicacion(String subicacion);
}
