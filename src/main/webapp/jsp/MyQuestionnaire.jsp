<%@ page import="jakarta.servlet.http.Cookie" %>

<%@ page import="java.sql.*, java.util.*, java.util.Base64, Model.Filter, Model.Profile" %>
<%@ page import="Model.Massage" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Profile Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100vh;
            position: relative;
        }
        .back-button {
            position: absolute;
            top: 10px;
            left: 10px;
            padding: 5px 10px;
            background-color: #000;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
        }
        .container {
            background-color: white;
            border: 2px solid #ccc;
            border-radius: 10px;
            padding: 20px;
            width: 400px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .profile {
            text-align: center;
            width: 100%;
        }
        .photo {
            background-color: #ccc;
            width: 100%;
            height: 300px;
            border-radius: 10px;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #666;
            font-size: 18px;
            overflow: hidden;
        }
        .photo img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        .info {
            text-align: left;
            font-size: 16px;
            color: #333;
            width: 100%;
        }
        .info p {
            margin: 5px 0;
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
        .view-other-button:hover {
            background-color: #f0f0f0;
        }
        .message-indicator {
            position: absolute;
            top: -10px;
            right: -10px;
            background-color: red;
            color: white;
            border-radius: 50%;
            padding: 5px 10px;
            font-size: 12px;
        }
        .button-container {
            position: relative;
        }
    </style>
</head>
<body>
<%
    String token = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
                break;
            }
        }
    }

    int messageCount = 0;
    if (token != null) {
        Massage massage = new Massage();
        List<Profile> profiles = massage.findAndReturnQuestionnaires(token);
        if (profiles != null) {
            messageCount = profiles.size();
        }

        String URL = "jdbc:mysql://localhost:3306/dating_site_db";
        String USERNAME = "root";
        String PASSWORD = "qwerty";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT name, age, city, description, media FROM questionnaire WHERE token = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, token);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String city = resultSet.getString("city");
                String description = resultSet.getString("description");
                Blob mediaBlob = resultSet.getBlob("media");
                byte[] mediaBytes = mediaBlob.getBytes(1, (int) mediaBlob.length());
                String base64Image = Base64.getEncoder().encodeToString(mediaBytes);
%>
<a class="back-button" href="javascript:history.back()">Back</a>
<div class="container">
    <div class="profile">
        <div class="photo">
            <% if (mediaBytes != null && mediaBytes.length > 0) { %>
            <img src="data:image/jpeg;base64,<%= base64Image %>" alt="Фото" />
            <% } else { %>
            Фото
            <% } %>
        </div>
        <div class="info">
            <p><%= name %>, <%= age %> - <%= city %></p>
            <p>Про себе: <%= description %></p>
        </div>
    </div>
</div>
<div class="button-container">
    <div class="view-other-button" style="width: 410px;" onclick="window.location.href='message'">
        Вас вподобали
        <% if (messageCount > 0) { %>
        <div class="message-indicator"><%= messageCount %></div>
        <% } %>
    </div>
</div>
<div class="view-other-button" style="width: 410px;" onclick="window.location.href='direct'">
    Повідомлення
</div>

<div class="view-other-button" style="width: 410px;" onclick="window.location.href='otherquestionnaires'">
    Дивитися анкети інших
</div>
<div class="view-other-button" style="width: 410px;" onclick="window.location.href='сhange-questionnaire'">
    Редагувати анкету
</div>
<script>
    function logout() {
        var now = new Date();
        now.setFullYear(now.getFullYear() - 1);
        var cookieStr = 'token=; expires=' + now.toUTCString() + '; path=/; domain=' + window.location.hostname;
        document.cookie = cookieStr;
        window.location.href = 'mainJSP';
    }
</script>

<div class="view-other-button" style="width: 410px;" onclick="logout()">
    Вийти
</div>

<%
} else {
%>
<div class="view-other-button" style="width: 410px;" onclick="window.location.href='questionnaire'">
    Створити анкету
</div>
<%
    }
} catch (Exception e) {
    e.printStackTrace();
%>
<p>Error: <%= e.getMessage() %></p>
<%
} finally {
    try {
        if (resultSet != null) resultSet.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
%>
<p>Error closing resources: <%= e.getMessage() %></p>
<%
        }
    }
} else {
%>
<p>Token not found in cookies.</p>
<%
    }
%>
</body>
</html>
