package ru.ylab.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Config class for db and migration
 */
@Configuration
public class JDBCConfig {
    private static String URL;
    private static String USER_NAME;
    private static String PASSWORD;

    /**
     * Do migration.
     */
    @Bean
    public void doMigration() {
        try {
            getJdbcPropertiesFromYml();
            Connection connection = connect();
            System.out.println("url=" + URL);
            Database database =
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName("monitoring-schema");
            database.setLiquibaseSchemaName("liquibase-schema");
            Liquibase liquibase =
                    new Liquibase("dev/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("dev");
            System.out.println("Migration is completed successfully");
            connection.close();
        } catch (LiquibaseException e) {
            throw new RuntimeException("Got SQL Exception in migration " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Connect connection.
     *
     * @return the connection
     */
    public Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets url.
     */
    private void getJdbcPropertiesFromYml() {
        try {
            Map<String, Object> mapObject = getYmlObject();
            Map<String, Object> getJDBCProperties = getBaseObject(mapObject);
            if (getJDBCProperties == null) {
                throw new Exception("some error message");
            }
            getPropertiesFromMap(getJDBCProperties);
            if (URL == null || URL.isBlank() || USER_NAME == null || USER_NAME.isBlank()
                    || PASSWORD == null || PASSWORD.isBlank()) {
                throw new Exception("some error message during migration");
            }
        } catch (Exception e) {
            throw new RuntimeException("some error message");
        }
    }

    /**
     * Gets yml object.
     *
     * @return the yml object
     */
    private Map<String,Object> getYmlObject() {
        YamlMapFactoryBean fBean = new YamlMapFactoryBean();
        fBean.setResources(new ClassPathResource("application.yml"));
        fBean.setSingleton(true);
        return fBean.getObject();
    }

    /**
     * Gets a base object.
     *
     * @param mapObject the map object
     * @return the base object
     */
    private LinkedHashMap<String,Object> getBaseObject(Map<String,Object> mapObject) {
        return  (LinkedHashMap<String,Object>)  mapObject.get("jdbc");
    }

    /**
     * Gets url from map.
     *
     * @param map the map
     */
    private void getPropertiesFromMap(Map<String,Object> map) {
        URL = (String) map.get("url");
        USER_NAME = (String) map.get("user");
        PASSWORD = (String) map.get("password");
    }
}
