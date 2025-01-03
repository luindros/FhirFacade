package com.luindros;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver"); // Handle ClassNotFoundException
            String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
            String username = "postgres";
            String password = "postgres";

            // Connect to the database
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            statement = connection.createStatement();

            // Perform desired database operations
            resultSet = statement.executeQuery("SELECT * FROM Patients;");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                System.out.println("ID: " + id + ", First Name: " + first_name + ", Last Name: " + last_name);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Handle ClassNotFoundException
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQLException
        } finally {
            // Close resources in the finally block
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle potential SQLException during close
            }
        }
    }
}
