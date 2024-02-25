package ru.ylab.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Config class for db and migration
 */
@Slf4j
@Configuration
@PropertySource(value = {"classpath:application.yaml"})
public class JDBCConfig {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.user}")
    private String user = "monitor";
    @Value("${spring.datasource.password}")
    private String password = "monitor";

    /**
     * Do migration.
     *
     * @return the spring liquibase
     */
    @Bean
    public SpringLiquibase doMigration() {
        log.info("url=" + url);
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:dev/changelog.xml");
        liquibase.setDefaultSchema("monitoring_schema");
        liquibase.setLiquibaseSchema("liquibase_schema");
        liquibase.setDataSource(dataSource());
        liquibase.setContexts("dev");
        log.info("Migration is completed successfully");
        return liquibase;
    }

    /**
     * Data source data source.
     *
     * @return the data source
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }
}
