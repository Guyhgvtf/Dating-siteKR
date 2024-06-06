package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Direct {
    private final String URL = "jdbc:mysql://localhost:3306/dating_site_db";
    private final String USERNAME = "root";
    private final String PASSWORD = "qwerty";

    public List<String> findOppositeTokens(String token) {
        List<String> oppositeTokens = new ArrayList<>();

        String query = "SELECT sender_token, recipient_token FROM message WHERE sender_token = ? OR recipient_token = ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, token);
            statement.setString(2, token);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String senderToken = resultSet.getString("sender_token");
                    String recipientToken = resultSet.getString("recipient_token");

                    if (token.equals(senderToken)) {
                        oppositeTokens.add(recipientToken);
                    } else if (token.equals(recipientToken)) {
                        oppositeTokens.add(senderToken);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return oppositeTokens;
    }

//    public static void main(String[] args) {
//        Direct direct = new Direct();
//        List<String> result = direct.findOppositeTokens("1");
//        for (String token : result) {
//            System.out.println(token);
//        }
//    }
}
