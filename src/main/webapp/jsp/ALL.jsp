<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Base64" %>
<html>
<head>
    <title>Анкети</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
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
        .profile {
            background-color: white;
            border: 2px solid #ccc;
            border-radius: 10px;
            padding: 20px;
            width: 400px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin: 20px 0;
            display: flex;
            flex-direction: column;
            align-items: center;
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
        .buttons {
            display: flex;
            justify-content: space-around;
            width: 100%;
            margin-top: 20px;
        }
        .button {
            border: 1px solid #ccc;
            border-radius: 10px;
            padding: 10px 20px;
            cursor: pointer;
            transition: background-color 0.3s;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 18px;
        }
        .button:hover {
            background-color: #f0f0f0;
        }
    </style>
</head>
<body>
<a href="#" class="back-button">Назад</a>
<h1>Анкети</h1>
<%
    String url = "jdbc:mysql://localhost:3306/dating_site_db";
    String username = "root";
    String password = "qwerty";
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url, username, password);
        stmt = conn.createStatement();
        String query = "SELECT name, age, city, lookingFor, description, media FROM questionnaire";
        rs = stmt.executeQuery(query);

        while (rs.next()) {
            String name = rs.getString("name");
            int age = rs.getInt("age");
            String city = rs.getString("city");
            String lookingFor = rs.getString("lookingFor");
            String description = rs.getString("description");
            Blob mediaBlob = rs.getBlob("media");
            String base64Image = null;

            if (mediaBlob != null) {
                byte[] mediaBytes = mediaBlob.getBytes(1, (int) mediaBlob.length());
                base64Image = Base64.getEncoder().encodeToString(mediaBytes);
            }
%>
<div class="profile">
    <div class="photo">
        <% if (base64Image == null) { %>
        Фото
        <% } else { %>
        <img src="data:image/jpeg;base64,<%= base64Image %>" alt="Фото">
        <% } %>
    </div>
    <div class="info">
        <p><strong><%= name %>, <%= age %> - <%= city %></strong></p>
        <p>Опис: <%= description %></p>
    </div>
    <div class="buttons">
        <div class="button">❤️ Вподобати</div>
        <div class="button">👎 Пропустити</div>
        <div class="button">💤 Відкласти</div>
    </div>
</div>
<%
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
%>
</body>
</html>
