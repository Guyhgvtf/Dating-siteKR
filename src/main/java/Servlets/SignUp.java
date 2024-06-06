package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUp extends HttpServlet {
    private final String URL = "jdbc:mysql://localhost:3306/dating_site_db";
    private final String USERNAME = "root";
    private final String PASSWORD = "qwerty";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");
        String email = request.getParameter("email");





            Connection connection = null;
            PreparedStatement stmt = null;

            try {

                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);


                String sql = "INSERT INTO users (username,email, password) VALUES (?, ?, ?)";
                stmt = connection.prepareStatement(sql);


                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, password);


                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    out.println("<h1 style='color: black; text-align: center;'>Registration successful!</h1>");
                    out.println("<script>");
                    out.println("setTimeout(function() { window.location.href = 'sign-inJSP'; }, 1000);");
                    out.println("</script>");
                }
                else {
                    out.println("<h1>Registration failed!</h1>");
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                out.println("<h1>Database error: " + e.getMessage() + "</h1>");
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

    }
}
