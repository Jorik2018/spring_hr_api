package gob.regionancash.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
            "gob.regionancash.hr",
            "gob.regionancash.remuneracion",
            "org.isobit.directory"
        },
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class ErpDataSourceConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.erp")
    public DataSource erpDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean erpEntityManager(EntityManagerFactoryBuilder builder) {
        
        System.out.println("=======>erpEntityManager");
        return builder
                .dataSource(erpDataSource())
                .packages(
                    "gob.regionancash.hr.escalafon.model", 
                    "gob.regionancash.hr.model",
                    "gob.regionancash.remuneracion.model",
                    "org.isobit.directory.model"
                )
                .persistenceUnit("erp")
                .build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager erpTransactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}