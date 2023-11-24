package catalogo.reportes.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "catalogo.reportes.core.catalogo.repository",
        mongoTemplateRef = "mongoTemplateCatalogo")
public class CatalogoMongoConfiguration {

}
