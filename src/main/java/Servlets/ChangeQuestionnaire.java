package Servlets;

import com.mysql.cj.jdbc.Driver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Types;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;


    @MultipartConfig
    public class ChangeQuestionnaire extends HttpServlet {
        private final String URL = "jdbc:mysql://localhost:3306/dating_site_db";
        private final String USERNAME = "root";
        private final String PASSWORD = "qwerty";


        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            String name = request.getParameter("name");
            int age = Integer.parseInt(request.getParameter("age"));
            String city = request.getParameter("city");
            String yourGender = request.getParameter("yourGender");
            String lookingFor = request.getParameter("lookingFor");
            String description = request.getParameter("description");
            String tokenValue = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("token")) {
                        tokenValue = cookie.getValue();
                        break;
                    }
                }
            }

            Part filePart = request.getPart("media");
            InputStream fileContent = filePart.getInputStream();
            String message = null;

            Connection connection = null;
            PreparedStatement deleteStatement = null;
            PreparedStatement statement = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connected to the database successfully.");


                String deleteSql = "DELETE FROM questionnaire WHERE token = ?";
                deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setString(1, tokenValue);
                int deletedRows = deleteStatement.executeUpdate();


                String sql = "INSERT INTO questionnaire (name, age, city,your_gender, lookingFor, description, media, token ) values (?, ?, ?, ?, ?, ?, ?, ?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, name);
                statement.setInt(2, age);
                statement.setString(3, city);
                statement.setString(4, yourGender );
                statement.setString(5, lookingFor);
                statement.setString(6, description);
                statement.setBlob(7, fileContent);
                statement.setString(8, tokenValue);


                int insertedRows = statement.executeUpdate();
                if (insertedRows > 0) {
                    message = "Анкету успішно оновлено!";
                }

            } catch (Exception ex) {
                message = "Сталася помилка: " + ex.getMessage();
                ex.printStackTrace();
            } finally {
                // Close resources
                if (fileContent != null) {
                    try {
                        fileContent.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (deleteStatement != null) {
                    try {
                        deleteStatement.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<h1>" + message + "</h1>");
            }
        }
    }