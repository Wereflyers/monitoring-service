package ru.ylab;

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
public class JDBCConfigTest {
    private static String URL;
    private static String USER_NAME;
    private static String PASSWORD;

    /**
     * Do migration.
     */
    public static void doMigration(String url, String name, String password) {
        try {
            URL = url;
            USER_NAME = name;
            PASSWORD = password;
            Connection connection = connect();
            System.out.println("url=" + URL);
            Database database =
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase =
                    new Liquibase("dev/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("test");
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
    public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
