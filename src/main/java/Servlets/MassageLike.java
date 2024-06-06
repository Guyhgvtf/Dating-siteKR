package Servlets;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;
import Model.Profile;
import Model.Massage;
import Model.ProfileResponse;

@WebServlet("/MassageLike")
public class MassageLike extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final String URL = "jdbc:mysql://localhost:3306/dating_site_db";
    private final String USERNAME = "root";
    private final String PASSWORD = "qwerty";
    private static final Logger LOGGER = Logger.getLogger(MassageLike.class.getName());

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
            Massage massage = new Massage();
            List<Profile> profiles = massage.findAndReturnQuestionnaires(userToken);

            if (index < profiles.size()) {
                Profile currentProfile = profiles.get(index);

                LOGGER.log(Level.INFO, "Index: {0}", index);
                LOGGER.log(Level.INFO, "Action: {0}", action);
                LOGGER.log(Level.INFO, "Profile Token: {0}", profileToken);
                LOGGER.log(Level.INFO, "Current Profile Token: {0}", currentProfile.getToken());


                if ("like".equals(action) && profileToken != null && profileToken.equals(currentProfile.getToken())) {
                    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
                        String insertLikeSQL = "INSERT INTO `message` (sender_token, recipient_token) VALUES (?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertLikeSQL)) {
                            preparedStatement.setString(1, userToken);
                            preparedStatement.setString(2, profileToken);
                            preparedStatement.executeUpdate();
                            LOGGER.log(Level.INFO, "Like inserted for sender: {0}, recipient: {1}", new Object[]{userToken, profileToken});
                        }
                        String deleteLikeSQL = "DELETE FROM `like` WHERE sender_token = ? AND recipient_token = ? AND status = ?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteLikeSQL)) {
                            preparedStatement.setString(1,profileToken);
                            preparedStatement.setString(2, userToken);
                            preparedStatement.setInt(3, 0);
                            int rowsDeleted = preparedStatement.executeUpdate();
                            LOGGER.log(Level.INFO, "Rows deleted: {0}", rowsDeleted);
                        }
                    } catch (SQLException e) {
                        LOGGER.log(Level.SEVERE, "Error inserting like", e);
                    }
                }

                if ("skip".equals(action) && profileToken != null && profileToken.equals(currentProfile.getToken())) {
                    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
                        String deleteLikeSQL = "DELETE FROM `like` WHERE sender_token = ? AND recipient_token = ? AND status = ?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteLikeSQL)) {
                            preparedStatement.setString(1,profileToken);
                            preparedStatement.setString(2, userToken);
                            preparedStatement.setInt(3, 0);
                            int rowsDeleted = preparedStatement.executeUpdate();
                            LOGGER.log(Level.INFO, "Rows deleted: {0}", rowsDeleted);
                        }
                    } catch (SQLException e) {
                        LOGGER.log(Level.SEVERE, "Error deleting like", e);
                    }
                }


                if (index + 1 < profiles.size()) {
                    Profile nextProfile = profiles.get(index + 1);

                    String base64Image = null;
                    Blob mediaBlob = nextProfile.getMedia();
                    if (mediaBlob != null) {
                        byte[] mediaBytes;
                        try {
                            mediaBytes = mediaBlob.getBytes(1, (int) mediaBlob.length());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        base64Image = Base64.getEncoder().encodeToString(mediaBytes);
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
