package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Massage {

    private final String URL = "jdbc:mysql://localhost:3306/dating_site_db";
    private final String USERNAME = "root";
    private final String PASSWORD = "qwerty";

    public List<Profile> findAndReturnQuestionnaires(String token) {
        Connection conn = null;
        PreparedStatement pstmtLike = null;
        PreparedStatement pstmtQuestionnaire = null;
        ResultSet rsLike = null;
        ResultSet rsQuestionnaire = null;
        List<Profile> profiles = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String queryLike = "SELECT sender_token FROM `like` WHERE recipient_token = ?";
            pstmtLike = conn.prepareStatement(queryLike);
            pstmtLike.setString(1, token);
            rsLike = pstmtLike.executeQuery();

            String queryQuestionnaire = "SELECT token, name, age, city, description, media FROM questionnaire WHERE token = ?";
            pstmtQuestionnaire = conn.prepareStatement(queryQuestionnaire);

            while (rsLike.next()) {
                String senderToken = rsLike.getString("sender_token");

                pstmtQuestionnaire.setString(1, senderToken);
                rsQuestionnaire = pstmtQuestionnaire.executeQuery();

                while (rsQuestionnaire.next()) {
                    Profile profile = new Profile();
                    profile.setToken(rsQuestionnaire.getString("token"));
                    profile.setName(rsQuestionnaire.getString("name"));
                    profile.setAge(rsQuestionnaire.getInt("age"));
                    profile.setCity(rsQuestionnaire.getString("city"));
                    profile.setDescription(rsQuestionnaire.getString("description"));
                    profile.setMedia(rsQuestionnaire.getBlob("media"));

                    profiles.add(profile);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsLike != null) rsLike.close();
                if (rsQuestionnaire != null) rsQuestionnaire.close();
                if (pstmtLike != null) pstmtLike.close();
                if (pstmtQuestionnaire != null) pstmtQuestionnaire.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return profiles;
    }

}
