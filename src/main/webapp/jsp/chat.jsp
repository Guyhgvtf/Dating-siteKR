<%@ page import="java.sql.*" %>
<%@ page import="Model.ChatTokenGenerator" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.Cookie" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.MessageSearch, Model.MessageList" %>

<%

    Cookie[] cookies = request.getCookies();
    String senderMessage = null;
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                senderMessage = cookie.getValue();
                break;
            }
        }
    }

    String recipientMessage = request.getParameter("token");
    String recipientName = request.getParameter("name");
    String message = request.getParameter("message");

    String chatToken = null;
    if (senderMessage != null && recipientMessage != null) {
        chatToken = ChatTokenGenerator.generateChatToken(senderMessage, recipientMessage);

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/dating_site_db";
            String username = "root";
            String password = "qwerty";
            conn = DriverManager.getConnection(dbURL, username, password);

            String sql = "INSERT INTO chat (sender_message, recipient_message, chat_token, message) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, senderMessage);
            pstmt.setString(2, recipientMessage);
            pstmt.setString(3, chatToken);
            pstmt.setString(4, message);
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


    MessageSearch search = new MessageSearch();
    List<MessageList> messages = search.findMessagesByChatToken(chatToken);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chat Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
        }
        #chat-container {
            display: flex;
            flex-direction: column;
            height: 100vh;
            justify-content: space-between;
        }
        #chat-header {
            background-color: #333;
            color: white;
            padding: 15px;
            text-align: center;
        }
        #chat-history {
            flex-grow: 1;
            padding: 20px;
            overflow-y: auto;
        }
        #chat-history ul {
            list-style-type: none;
            padding: 0;
        }
        #chat-history li {
            margin: 10px 0;
            padding: 10px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        #chat-footer {
            padding: 10px;
            display: flex;
            align-items: center;
            background-color: #eee;
        }
        #messageInput {
            flex-grow: 1;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-right: 10px;
        }
        #sendButton {
            background-color: #333; /* Light black */
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 5px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div id="chat-container">
    <div id="chat-header">
        <h1><%= recipientName %></h1>
    </div>
    <div id="chat-history">
        <% if (messages != null && !messages.isEmpty()) { %>
        <ul id="messageList">
            <% for (MessageList message1 : messages) { %>
            <li><strong><%= message1.getSenderMessageName() %>:</strong> <%= message1.getMessage() %></li>
            <% } %>
        </ul>
        <% } else { %>
        <p>No messages found.</p>
        <% } %>
    </div>
    <div id="chat-footer">
        <input type="text" id="messageInput" placeholder="Type a message">
        <button id="sendButton">Send</button>
    </div>
</div>

<script>
    var ws = new WebSocket('ws://localhost:8080/DatingSite_war_exploded/chat');
    var messageList = document.getElementById('messageList');

    ws.onmessage = function(event) {
        var message = document.createElement('li');
        message.textContent = event.data;
        messageList.appendChild(message);
        location.reload();
    };

    document.getElementById('messageInput').addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            document.getElementById('sendButton').click();
        }
    });

    document.getElementById('sendButton').onclick = function() {
        var messageInput = document.getElementById('messageInput');
        var message = messageInput.value;
        var senderToken = '<%= senderMessage %>';
        var recipientToken = '<%= recipientMessage %>';
        if (message) {
            ws.send(senderToken + ':' + recipientToken + ':' + message);
            messageInput.value = '';
        }
    };
</script>
</body>
</html>
