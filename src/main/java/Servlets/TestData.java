package Servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestData {
    private static final String URL = "jdbc:mysql://localhost:3306/dating_site_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "qwerty";

    public static void main(String[] args) {
        Connection connection = null;

        try {

            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database successfully.");
        } catch (SQLException e) {

            e.printStackTrace();
            throw new RuntimeException("Could not connect to the database.", e);
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

