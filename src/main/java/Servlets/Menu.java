package Servlets;

import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;

public class Menu extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] token = request.getCookies();
        PrintWriter out =response.getWriter();

        out.println("<html><body>");
        out.println("<h1>" + "Hello " + "</h1>");
        if (token != null) {
            out.println("<h2>Cookies:</h2>");
            out.println("<ul>");
            for (Cookie cookie : token) {
                out.println("<li>" + cookie.getName() + ": " + cookie.getValue() + "</li>");
            }
            out.println("</ul>");
        }
        out.println("</body></html>");



    }
}
