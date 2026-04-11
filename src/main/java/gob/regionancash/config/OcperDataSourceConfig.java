package gob.regionancash.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
        basePackages = "edu.uns.remuneracion.repository",
        entityManagerFactoryRef = "ocperEntityManager",
        transactionManagerRef = "ocperTransactionManager"
)
public class OcperDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.ocper")
    public DataSource ocperDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean ocperEntityManager(EntityManagerFactoryBuilder builder) {
        System.out.println("=======>ocperEntityManager");
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "update");
        return builder
                .dataSource(ocperDataSource())
                .packages("edu.uns.remuneracion.model")
                .persistenceUnit("ocper")
                .properties(props)
                .build();
    }

    @Bean
    public PlatformTransactionManager ocperTransactionManager(
            @Qualifier("ocperEntityManager") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}