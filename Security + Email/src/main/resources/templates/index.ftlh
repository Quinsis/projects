<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Главная страница</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/style.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
</head>
<body>
    <header class="header">
        <div class="header__logo">
            <img src="logo.png" alt="Логотип">
        </div>
        <nav class="header__nav">
            <ul class="nav">

            </ul>
        </nav>
        <#if isAuthenticated>
        <a href="/account" class="header__action">Личный кабинет</a>
        <#else>
            <a href="/login" class="header__action">Авторизация</a>
        </#if>
    </header>
    <i id="theme-toggle" class="fa-solid fa-moon"></i>
</body>
<script src="/scripts/index.js"></script>
</html>