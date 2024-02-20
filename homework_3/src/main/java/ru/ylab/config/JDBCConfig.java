package ru.ylab.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Config class for db and migration
 */
public class JDBCConfig {
    private static String URL = "jdbc:postgresql://localhost:5432/monitoring?currentSchema=monitoring-schema";
    private static String USER_NAME = "monitor";
    private static String PASSWORD = "monitor";

    /**
     * Instantiates a new Jdbc config.
     */
    public JDBCConfig() {
        doMigration();
    }

    /**
     * Do migration.
     */
    private void doMigration() {
        try {
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
     * Load properties.
     */
    public void loadProperties() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(getClass().getResource("application.properties").getPath()));
            URL = props.getProperty("jdbc.url");
            USER_NAME = props.getProperty("jdbc.user");
            PASSWORD = props.getProperty("jdbc.password");
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
