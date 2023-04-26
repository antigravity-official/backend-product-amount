package antigravity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages="antigravity.rds.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager")
public class JpaConfig {
    private static final String DEFAULT_NAMING_STRATEGY
            = "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy";

    @Autowired
    private MyBatisConfig myBatisConfig;

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder) {

        Map<String, String> propertiesHashMap = new HashMap<>();
        propertiesHashMap.put("hibernate.physical_naming_strategy",DEFAULT_NAMING_STRATEGY);
        LocalContainerEntityManagerFactoryBean rep =
                builder.dataSource(myBatisConfig.dataSource())
                        .packages("antigravity.rds.entity")
                        .properties(propertiesHashMap)
                        .build();

        return rep;
    }
}
