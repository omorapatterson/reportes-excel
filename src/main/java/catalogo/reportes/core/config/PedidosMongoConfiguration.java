package catalogo.reportes.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "catalogo.reportes.core.pedidos.pedidosRepository",
        mongoTemplateRef = "mongoTemplatePedidos")
public class PedidosMongoConfiguration {

}
