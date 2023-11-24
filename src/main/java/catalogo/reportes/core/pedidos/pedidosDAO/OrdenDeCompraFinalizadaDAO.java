package catalogo.reportes.core.pedidos.pedidosDAO;

import catalogo.reportes.core.pedidos.pedidosEntity.OrdenDeCompraFinalizada;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;


import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
public class OrdenDeCompraFinalizadaDAO {

    private final MongoOperations mongoOperations;

    OrdenDeCompraFinalizadaDAO(@Qualifier("mongoTemplatePedidos") MongoOperations mongoOperations ) {
        this.mongoOperations = mongoOperations;
    }

    public List<OrdenDeCompraFinalizada> obtenerEmpresasEmitiendoOrdenesDeCompra(DateTime fechaDesde, DateTime fechaHasta) {

        Aggregation empresasConOrdenesDeCompra = Aggregation.newAggregation(
                match(Criteria.where("eliminado").is(false).andOperator(Criteria.where("fechaCreacion").gte(fechaDesde), Criteria.where("fechaCreacion").lte(fechaHasta))),
                project().andExpression("empresa").as("empresa"),
                group(fields().and("empresa"))
        );
        List<OrdenDeCompraFinalizada> empresasEmitiendoOrdenesDeCompra = mongoOperations.aggregate(empresasConOrdenesDeCompra, "OrdenDeCompraFinalizada", OrdenDeCompraFinalizada.class).getMappedResults();
        return empresasEmitiendoOrdenesDeCompra;
    }

    public List<OrdenDeCompraFinalizada> obtenerEmpresasQueRecibenOrdenesDeCompra(DateTime fechaDesde, DateTime fechaHasta) {

        Aggregation empresasConOrdenesDeCompra = Aggregation.newAggregation(
                match(Criteria.where("eliminado").is(false).andOperator(Criteria.where("fechaCreacion").gte(fechaDesde), Criteria.where("fechaCreacion").lte(fechaHasta))),
                project().andExpression("proveedor").as("proveedor"),
                group(fields().and("proveedor"))
        );
        List<OrdenDeCompraFinalizada> empresasEmitiendoOrdenesDeCompra = mongoOperations.aggregate(empresasConOrdenesDeCompra, "OrdenDeCompraFinalizada", OrdenDeCompraFinalizada.class).getMappedResults();
        return empresasEmitiendoOrdenesDeCompra;
    }

}

