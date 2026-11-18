
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/pairup";

    private static final String USER = "root";
    private static final String PASSWORD =
        System.getenv("PAIRUP_DB_PASSWORD");

    public static Connection getConnection() {

        Connection connection = null;

        try {

            connection = DriverManager.getConnection(
                    URL, USER, PASSWORD
            );

            System.out.println(
                    "Database connected successfully!"
            );

        } catch (SQLException e) {

            System.out.println(
                    "Database connection failed!"
            );

            e.printStackTrace();
        }

        return connection;
    }

    public static void main(String[] args) {

        Connection connection = getConnection();

        if (connection != null) {

            try {
                connection.close();
                System.out.println(
                        "Database connection closed!"
                );

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
