package Servlets;

import Model.ProfileResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Base64;
import java.util.List;
import com.google.gson.Gson;
import Model.Filter;
import Model.Profile;

@WebServlet("/NextProfileServlet")
public class NextProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final String URL = "jdbc:mysql://localhost:3306/dating_site_db";
    private final String USERNAME = "root";
    private final String PASSWORD = "qwerty";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String userToken = null;
        int index = Integer.parseInt(request.getParameter("index"));
        String action = request.getParameter("action");
        String profileToken = request.getParameter("profileToken");

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    userToken = cookie.getValue();
                    break;
                }
            }
        }

        if (userToken != null) {
            Filter filter = new Filter();
            List<Profile> profiles = filter.getFilteredProfiles(userToken);

            if (index < profiles.size()) {
                Profile currentProfile = profiles.get(index);

                System.out.println("Index: " + index);
                System.out.println("Action: " + action);
                System.out.println("Profile Token: " + profileToken);
                System.out.println("Current Profile Token: " + currentProfile.getToken());

                if ("like".equals(action) && profileToken != null && profileToken.equals(currentProfile.getToken())) {
                    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
                        String checkLikeSQL = "SELECT COUNT(*) FROM `like` WHERE sender_token = ? AND recipient_token = ?";
                        try (PreparedStatement checkStatement = connection.prepareStatement(checkLikeSQL)) {
                            checkStatement.setString(1, userToken);
                            checkStatement.setString(2, profileToken);
                            try (ResultSet resultSet = checkStatement.executeQuery()) {
                                if (resultSet.next() && resultSet.getInt(1) == 0) {
                                    String insertLikeSQL = "INSERT INTO `like` (sender_token, recipient_token, status) VALUES (?, ?, ?)";
                                    try (PreparedStatement insertStatement = connection.prepareStatement(insertLikeSQL)) {
                                        insertStatement.setString(1, userToken);
                                        insertStatement.setString(2, profileToken);
                                        insertStatement.setInt(3, 0);
                                        insertStatement.executeUpdate();
                                    }
                                }
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (index + 1 < profiles.size()) {
                    Profile nextProfile = profiles.get(index + 1);

                    String base64Image = null;
                    Blob mediaBlob = nextProfile.getMedia();
                    if (mediaBlob != null) {
                        try {
                            byte[] mediaBytes = mediaBlob.getBytes(1, (int) mediaBlob.length());
                            base64Image = Base64.getEncoder().encodeToString(mediaBytes);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    ProfileResponse profileResponse = new ProfileResponse(
                            nextProfile.getName(),
                            nextProfile.getAge(),
                            nextProfile.getCity(),
                            nextProfile.getDescription(),
                            base64Image,
                            nextProfile.getToken()
                    );

                    Gson gson = new Gson();
                    String json = gson.toJson(profileResponse);
                    out.write(json);
                } else {
                    out.write("null");
                }
            } else {
                out.write("null");
            }
        }
    }
}
