<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign in</title>
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
<div class="form-container">
    <h1>Sign in</h1>
    <form action="sign-in" method="post">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" required>
        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>
        <button type="submit">Register</button>
    </form>
</div>
</body>
</html>
