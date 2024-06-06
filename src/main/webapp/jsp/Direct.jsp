<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Direct" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.*" %>
<%@ page import="jakarta.servlet.http.Cookie" %>
<%@ page import="java.net.URLEncoder" %>

<html>
<head>
    <title>Display Names</title>
    <style>
        body {
            color: white;
        }
        .center-message {
            background-color: grey;
            padding: 10px;
            margin: 5px;
            border-radius: 15px;
            text-align: center;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            color: white;
        }
        .view-other-button {
            background-color: white;
            border: 1px solid #ccc;
            border-radius: 15px;
            padding: 15px;
            width: 100%;
            text-align: center;
            cursor: pointer;
            transition: background-color 0.3s;
            margin-top: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            position: relative;
        }
        .name-display {
            background-color: grey;
            padding: 20px;
            margin: 5px;
            border-radius: 15px;
            color: white;
            text-decoration: none;
            display: block;
            width: calc(100% - 40px);
            box-sizing: border-box;
        }
        .name-display:hover {
            background-color: darkgrey;
        }
    </style>
</head>
<body>
<%

    String token = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }
    }

    if (token != null) {
        Direct direct = new Direct();
        List<String> oppositeTokens = direct.findOppositeTokens(token);

        if (oppositeTokens.isEmpty()) {

%>
<div class="center-message">
    У вас немає чатів
</div>
<%
} else {

    String URL = "jdbc:mysql://localhost:3306/dating_site_db";
    String USERNAME = "root";
    String PASSWORD = "qwerty";


    String nameQuery = "SELECT name FROM questionnaire WHERE token = ?";

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
        for (String oppositeToken : oppositeTokens) {
            try (PreparedStatement statement = connection.prepareStatement(nameQuery)) {
                statement.setString(1, oppositeToken);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String encodedName = URLEncoder.encode(name, "UTF-8");
%>
<a href="chat1?token=<%= oppositeToken %>&name=<%= encodedName %>" class="name-display">
    <%= name %>
</a>
<%
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} else {
%>
<div class="center-message">
    No token found in cookies.
</div>
<%
    }
%>
</body>
</html>
