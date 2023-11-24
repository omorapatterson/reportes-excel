package catalogo.reportes.core.catalogo.db;

import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.ListaDeVentaVisibilidad;
import common.rondanet.catalogo.core.entity.TraficoDeAplicacion;
import common.rondanet.catalogo.core.entity.Usuario;
import catalogo.reportes.core.catalogo.repository.ITraficoDeAplicacionRepository;
import com.mongodb.DBObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;


@Component
public class TraficoDeAplicacionDAO {

    @Autowired
    ITraficoDeAplicacionRepository iTraficoDeAplicacionRepository;

    private final MongoOperations mongoOperations;

    public TraficoDeAplicacionDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations){
        this.mongoOperations = mongoOperations;
    }

    public List<TraficoDeAplicacion> obtenerTodosTraficoDeAplicacionPorAccion(DateTime fechaInicial, DateTime fechaFinal){
        Query query = new Query();
        if(fechaInicial != null)
            query.addCriteria(new Criteria().and("fechaEdicion").gte(fechaInicial).lte(fechaFinal));
        if(fechaFinal != null && fechaInicial == null)
            query.addCriteria(new Criteria().and("fechaEdicion").lte(fechaFinal));
        query.with(Sort.by(Sort.Direction.DESC, "fechaEdicion"));
        List<TraficoDeAplicacion> listaTraficoDeAplicacion = mongoOperations.find(query, TraficoDeAplicacion.class);
        return listaTraficoDeAplicacion;
    }

    public List<TraficoDeAplicacion> obtenerTodosLosUsuariosQueEntraronALaAplicacion(
            DateTime fechaInicial, DateTime fechaFinal
    ){

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("eliminado").is(false), Criteria.where("fechaCreacion").gte(fechaInicial), Criteria.where("fechaCreacion").lte(fechaFinal));

        Aggregation traficoDeAplicacionAggregation = Aggregation.newAggregation(
                match(criteria),
                Aggregation.sort(Sort.Direction.ASC, "fechaCreacion"),
                group("sUsuario")
        );

        List<TraficoDeAplicacion> listaTraficoDeAplicacion = mongoOperations.aggregate(traficoDeAplicacionAggregation, "traficoDeAplicacion", TraficoDeAplicacion.class).getMappedResults();
        return listaTraficoDeAplicacion;
    }

    public Optional<TraficoDeAplicacion> obtenerTraficoDeAplicacionPorUsuario(
            String usuarioId,
            DateTime fechaInicial,
            DateTime fechaFinal
    ) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("eliminado").is(false), Criteria.where("sUsuario").is(usuarioId), Criteria.where("fechaCreacion").gte(fechaInicial), Criteria.where("fechaCreacion").lte(fechaFinal));

        Aggregation traficoDeAplicacionAggregation = Aggregation.newAggregation(
                match(criteria),
                Aggregation.sort(Sort.Direction.ASC, "fechaCreacion"),
                Aggregation.skip(0),
                Aggregation.limit(1)
        );
        List<TraficoDeAplicacion> listaTraficoDeAplicacion = mongoOperations.aggregate(traficoDeAplicacionAggregation, "traficoDeAplicacion", TraficoDeAplicacion.class).getMappedResults();

        return !listaTraficoDeAplicacion.isEmpty() ? Optional.of(listaTraficoDeAplicacion.get(0)) : Optional.ofNullable(null);
    }
}
