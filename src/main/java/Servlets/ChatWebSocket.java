package Servlets;

import Model.ChatTokenGenerator;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/chat")
public class ChatWebSocket {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, NoSuchAlgorithmException {
        for (Session s : sessions) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText(message);
            }
        }
        saveMessageToDatabase(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    private void saveMessageToDatabase(String message) throws NoSuchAlgorithmException {
        // Парсинг повідомлення. Наприклад, очікуємо, що повідомлення має формат "senderToken:recipientToken:messageContent"
        String[] parts = message.split(":", 3);
        if (parts.length < 3) {
            System.out.println("Incorrect message format.");
            return;
        }

        String senderToken = parts[0];
        String recipientToken = parts[1];
        String messageContent = parts[2];

        String chatToken = ChatTokenGenerator.generateChatToken(senderToken, recipientToken);

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/dating_site_db";
            String username = "root";
            String password = "qwerty";
            conn = DriverManager.getConnection(dbURL, username, password);
            System.out.println("Конект успішний");

            String sql = "INSERT INTO chat (sender_message, recipient_message, chat_token, message) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, senderToken);
            pstmt.setString(2, recipientToken);
            pstmt.setString(3, chatToken);
            pstmt.setString(4, messageContent);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}