<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Створення анкети</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin: 0;
                background-color: #f9f9f9;
            }
            .form-container {
                background-color: white;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                padding: 20px;
                width: 400px;
                text-align: center;
            }
            .form-container h1 {
                margin-bottom: 20px;
            }
            .form-container label {
                display: block;
                text-align: left;
                margin: 10px 0 5px;
                font-weight: bold;
            }
            .form-container input,
            .form-container textarea {
                width: calc(100% - 20px);
                padding: 10px;
                margin: 5px 0 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }
            .form-container input[type="file"] {
                padding: 3px;
            }
            .form-container .radio-group {
                display: flex;
                justify-content: space-between;
                margin: 5px 0 10px;
            }
            .form-container .radio-group input[type="radio"] {
                display: none;
            }
            .form-container .radio-group label {
                flex: 1;
                text-align: center;
                padding: 10px 0;
                margin: 0 5px;
                border: 1px solid #ccc;
                border-radius: 30px;
                background-color: #fff;
                cursor: pointer;
                transition: background-color 0.3s, color 0.3s;
            }
            .form-container .radio-group input[type="radio"]:checked + label {
                background-color: #000;
                color: #fff;
                border-color: #000;
            }
            .form-container button {
                width: 100%;
                padding: 10px;
                margin: 10px 0;
                background-color: #000;
                border: none;
                border-radius: 5px;
                color: white;
                font-size: 16px;
                cursor: pointer;
            }
            .form-container button:hover {
                background-color: #333;
            }
            .back-button {
                position: absolute;
                top: 20px;
                left: 20px;
                background-color: black;
                color: white;
                border: none;
                padding: 10px;
                border-radius: 5px;
                cursor: pointer;
            }
        </style>
    </head>
<body>

<button class="back-button" onclick="history.back()">Back</button>

<div class="form-container">
    <h1>Створення анкети</h1>
    <form action="questionnaireSR" method="post"  enctype="multipart/form-data">
        <label for="name">Ім'я</label>
        <input type="text" id="name" name="name" placeholder="Введіть ваше ім'я" required>

        <label for="age">Вік</label>
        <input type="number" id="age" name="age" placeholder="Введіть ваш вік" required>

        <label for="city">Місто</label>
        <input type="text" id="city" name="city" placeholder="Введіть ваше місто" required>

        <label>Ваш гендер</label>
        <div class="radio-group">
            <input type="radio" id="Boy" name="yourGender" value="Хлопця" required>
            <label for="Boy">Хлопець</label>
            <input type="radio" id="Girl" name="yourGender" value="Дівчину" required>
            <label for="Girl">Дівчина</label>
            <input type="radio" id="Any" name="yourGender" value="Без різниці" required>
            <label for="Any">Інше</label>
        </div>

        <label>Кого шукаєте</label>
        <div class="radio-group">
            <input type="radio" id="lookingForBoy" name="lookingFor" value="Хлопця" required>
            <label for="lookingForBoy">Хлопця</label>
            <input type="radio" id="lookingForGirl" name="lookingFor" value="Дівчину" required>
            <label for="lookingForGirl">Дівчину</label>
            <input type="radio" id="lookingForAny" name="lookingFor" value="Без різниці" required>
            <label for="lookingForAny">Без різниці</label>
        </div>

        <label for="description">Опис</label>
        <textarea id="description" name="description" placeholder="Введіть опис" rows="4" required></textarea>

        <label for="media">Додайте своє фото</label>
        <input type="file" id="media" name="media" accept="image/*,video/*">

        <button type="submit">Створити анкету</button>
    </form>
</div>

</body>
</html>