package catalogo.reportes.core.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.ws.rs.DefaultValue;

@Configuration
public class MultipleMongoConfiguration {

    @DefaultValue("pedidos")
    @Value("${mongodb.pedidos.database}")
    private String pedidosDataBase;

    @DefaultValue("mongodb://localhost:27017")
    @Value("${mongodb.pedidos.uri}")
    private String pedidosUri;

    @DefaultValue("catalogo")
    @Value("${mongodb.catalogo.database}")
    private String catalogoDataBase;

    @DefaultValue("mongodb://localhost:27017")
    @Value("${mongodb.catalogo.uri}")
    private String catalogoUri;

    public MultipleMongoConfiguration() {
    }

    public @Bean MongoClient mongoClientCatalogo() {
        return MongoClients.create(catalogoUri);
    }

    public @Bean MongoClient mongoClientPedido() {
        return MongoClients.create(pedidosUri);
    }

    @Bean(name = "mongoTemplateCatalogo")
    public MongoTemplate mongoTemplateCatalogo() {
        return new MongoTemplate(mongoClientCatalogo(), catalogoDataBase);
    }

    @Bean(name = "mongoTemplatePedidos")
    public MongoTemplate mongoTemplatePedidos() {
        return new MongoTemplate(mongoClientPedido(), pedidosDataBase);
    }
}
