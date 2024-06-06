package Servlets;

import Model.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.PrintWriter;
import java.sql.*;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SignIn extends HttpServlet {
    private final String URL = "jdbc:mysql://localhost:3306/dating_site_db";
    private final String USERNAME = "root";
    private final String PASSWORD = "qwerty";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Connection connection = null;

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database successfully.");

            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                out.println("User authenticated successfully.");
                String jwtToken = resultSet.getString("token");

                if (jwtToken == null || jwtToken.isEmpty()) {
                    jwtToken = JwtUtil.generateToken(email);
                    String updateTokenQuery = "UPDATE users SET token = ? WHERE email = ?";
                    PreparedStatement updateTokenStatement = connection.prepareStatement(updateTokenQuery);
                    updateTokenStatement.setString(1, jwtToken);
                    updateTokenStatement.setString(2, email);
                    updateTokenStatement.executeUpdate();
                }

                Cookie token = new Cookie("token", jwtToken);
                Cookie name = new Cookie("name", email);

                name.setMaxAge(234324);
                token.setMaxAge(24 * 60 * 60);

                token.setPath("/");
                name.setPath("/");

                response.addCookie(token);
                response.addCookie(name);

                response.sendRedirect("myquestionnaire");
            } else {
                out.println("Invalid email or password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Could not connect to the database.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            out.println("Database driver not found.");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            out.close();
        }
    }
}
