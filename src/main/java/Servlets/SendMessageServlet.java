package Servlets;



import java.io.*;
import javax.servlet.*;

import javax.servlet.http.*;
import java.sql.*;


public class SendMessageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String message = request.getParameter("message");
        String senderToken = null;


        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    senderToken = cookie.getValue();
                    break;
                }
            }
        }

        if (senderToken != null && !senderToken.isEmpty()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dating_site_db", "root", "qwerty");
                String sql = "INSERT INTO chat (sender_message, recipient_message, chat_token, message) VALUES (?, NULL, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, senderToken);
                stmt.setString(2, message);
                stmt.executeUpdate();
                conn.close();
                response.sendRedirect("chat.jsp");
            } catch (Exception e) {
                out.println("Помилка: " + e.getMessage());
                e.printStackTrace(out);
            }
        } else {
            out.println("Помилка: токен не знайдено.");
        }
    }
}
