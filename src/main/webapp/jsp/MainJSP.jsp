<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Discover Your Perfect Match</title>
    <style>
        body, html {
            margin: 0;
            padding: 0;
            width: 100%;
            height: 100%;
            font-family: Arial, sans-serif;
        }

        .container {
            background-image: url('https://cdn.gamma.app/y7lfx292mpmjhhn/generated-images/6-HnUTZKaywq5J6R.jpg');
            background-size: cover;
            background-position: center;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: black;
            text-align: center;
            position: relative;
        }

        .container::before {
            content: "";
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            z-index: 1;
        }

        .content {
            position: relative;
            z-index: 2;
            background: rgba(255, 255, 255, 0.9);
            padding: 50px;
            border-radius: 10px;
        }

        h1 {
            font-size: 3em;
            margin-bottom: 20px;
        }

        p {
            font-size: 1.2em;
            margin-bottom: 40px;
        }

        a {
            text-decoration: none;
            color: black;
            padding: 10px 20px;
            margin: 0 10px;
            border: 1px solid black;
            border-radius: 5px;
            transition: background 0.3s, color 0.3s;
        }

        a:hover {
            background: black;
            color: white;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="content">
        <h1>Discover Your Perfect Match</h1>
        <p>A vibrant community where you can find love and connection.</p>
        <a href="sign-inJSP">Sign in</a>
        <a href="sign-upJSP">Sign up</a>
    </div>
</div>
</body>
</html>
