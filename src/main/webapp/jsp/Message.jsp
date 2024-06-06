<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, java.util.*, java.util.Base64, Model.Profile" %>
<%@ page import="jakarta.servlet.http.Cookie" %>
<%@ page import="Model.Massage" %>
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
            border: solid 2px #ccc;
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
    <script>
        let currentIndex = 0;
        let currentProfileToken = null;

        function skipProfile(action) {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "MassageLike?index=" + currentIndex + "&action=" + action + "&profileToken=" + currentProfileToken, true);
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    var profile = JSON.parse(xhr.responseText);
                    if (profile) {
                        currentProfileToken = profile.token;
                        document.getElementById("name").innerText = profile.name;
                        document.getElementById("age").innerText = profile.age;
                        document.getElementById("city").innerText = profile.city;
                        document.getElementById("description").innerText = profile.description;
                        var photo = document.getElementById("photo");
                        if (profile.base64Image) {
                            photo.innerHTML = '<img src="data:image/jpeg;base64,' + profile.base64Image + '" alt="Фото">';
                        } else {
                            photo.innerHTML = 'Фото';
                        }
                        currentIndex++;
                    } else {
                        document.querySelector(".profile").innerHTML = '<p>Ви переглянули усі доступні анкети.</p>';
                    }
                }
            };
            xhr.send();
        }
    </script>


</head>
<body>
<a href="myquestionnaire" class="back-button">Назад</a>
<h1>Анкети</h1>
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
    if (token != null) {
        Massage massage = new Massage();
        List<Profile> profiles = massage.findAndReturnQuestionnaires(token);
        Profile profile = profiles.size() > 0 ? profiles.get(0) : null;
        if (profile != null) {
            String name = profile.getName();
            int age = profile.getAge();
            String city = profile.getCity();
            String description = profile.getDescription();
            Blob mediaBlob = profile.getMedia();
            String base64Image = null;
            if (mediaBlob != null) {
                byte[] mediaBytes = mediaBlob.getBytes(1, (int) mediaBlob.length());
                base64Image = Base64.getEncoder().encodeToString(mediaBytes);
            }
%>
<script>
    currentProfileToken = "<%= profile.getToken() %>";
</script>
<div class="profile">
    <div class="photo" id="photo">
        <% if (base64Image == null) { %> Фото <% } else { %> <img src="data:image/jpeg;base64,<%= base64Image %>" alt="Фото"> <% } %>
    </div>
    <div class="info">
        <p><strong id="name"><%= name %></strong>, <span id="age"><%= age %></span> - <span id="city"><%= city %></span></p>
        <p>Опис: <span id="description"><%= description %></span></p>
    </div>
    <div class="buttons">
        <div class="button" onclick="skipProfile('like')">❤️ Вподобати</div>
        <div class="button" onclick="skipProfile('skip')">👎 Пропустити</div>
        <div class="button" onclick="window.location.href = 'myquestionnaire'">💤 Відкласти</div>
    </div>
</div>
<%
} else {
%>
<p>Анкети не знайдено.</p>
<%
    }
} else {
%>
<p>Маркер не знайдено. Будь ласка, увійдіть.</p>
<% } %>
</body>
</html>
