package Model;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static Model.ChatTokenGenerator.generateChatToken;

public class MessageSearch {
    private static final String URL = "jdbc:mysql://localhost:3306/dating_site_db";
    private static final String USER = "root";
    private static final String PASSWORD = "qwerty";

    public List<MessageList> findMessagesByChatToken(String chatToken) {
        List<MessageList> messages = new ArrayList<>();

        String sql = "SELECT q.name as senderMessageName, c.sender_message, c.recipient_message, c.chat_token, c.message " +
                "FROM chat c " +
                "JOIN questionnaire q ON c.sender_message = q.token " +
                "WHERE c.chat_token = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, chatToken);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String senderMessageName = resultSet.getString("senderMessageName");
                String senderMessage = resultSet.getString("sender_message");
                String recipientMessage = resultSet.getString("recipient_message");
                String messageText = resultSet.getString("message");
                MessageList message = new MessageList(senderMessageName, senderMessage, recipientMessage, chatToken, messageText);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {
        String senderMessage = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMUAxMSIsImlzcyI6IllvdXJBcHBOYW1lIiwiZXhwIjoxNzE2NTU1MDc2fQ.L-rjKJpTHw-UMJfWtPQ_hBQR8WOFXpdBaOh8WtVwWTY";
        String recipientMessage = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyQDIiLCJpc3MiOiJZb3VyQXBwTmFtZSIsImV4cCI6MTcxNjU1NTEwOH0.8GiOt7Z7vJ7n-GL20V85ok0GJ6Vj_Y96O5mgjBBu0Qg";
        String chatToken = generateChatToken(recipientMessage, senderMessage);

        MessageSearch search = new MessageSearch();
        List<MessageList> messages = search.findMessagesByChatToken(chatToken);

        for (MessageList message : messages) {
            System.out.println("Sender Name: " + message.getSenderMessageName());
//            System.out.println("Sender Message: " + message.getSenderMessage());
//            System.out.println("Recipient Message: " + message.getRecipientMessage());
            System.out.println("Message Text: " + message.getMessage());
//            System.out.println("Chat Token: " + message.getChatToken());
            System.out.println("-----------------------------------");
        }
    }
}
