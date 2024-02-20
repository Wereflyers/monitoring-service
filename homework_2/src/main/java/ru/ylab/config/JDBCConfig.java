package ru.ylab.config;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Config class for db and migration
 */
public class JDBCConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/monitoring?currentSchema=monitoring-schema";
    private static final String USER_NAME = "monitor";
    private static final String PASSWORD = "monitor";
    private static Connection connection;

    /**
     * Do migration.
     */
    public static void doMigration() {
        try {
            Database database =
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName("monitoring-schema");
            database.setLiquibaseSchemaName("liquibase-schema");
            Liquibase liquibase =
                    new Liquibase("dev/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts());
            System.out.println("Migration is completed successfully");
        } catch (LiquibaseException e) {
            throw new RuntimeException("Got SQL Exception in migration " + e.getMessage());
        }
    }

    /**
     * Connect connection.
     *
     * @return the connection
     */
    public static Connection connect() {
        return connection;
    }

    public static void doConnect() {
        try {
            System.out.println("url=" + URL);
            connection = DriverManager.getConnection(
                    URL, USER_NAME, PASSWORD
            );
            doMigration();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
