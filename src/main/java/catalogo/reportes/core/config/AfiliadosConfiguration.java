package catalogo.reportes.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(
        basePackages = "catalogo.reportes.core.afiliados.afiliadosRepositories",
        entityManagerFactoryRef = "afiliadosEntityManager",
        transactionManagerRef = "afiliadosTransactionManager")
public class AfiliadosConfiguration {

    @Autowired
    private Environment env;

    @Bean(name = "afiliadosDataSource")
    @ConfigurationProperties(prefix="afiliados.datasource")
    public DataSource afiliadosDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "afiliadosEntityManager")
    public LocalContainerEntityManagerFactoryBean afiliadosEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(afiliadosDataSource());
        em.setPackagesToScan(
                new String[] { "common.rondanet.clasico.core.afiliados.models" });
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
                env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "afiliadosTransactionManager")
    public PlatformTransactionManager afiliadosTransactionManager() {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                afiliadosEntityManager().getObject());
        return transactionManager;
    }
}