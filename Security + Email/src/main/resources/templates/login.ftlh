<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Авторизация</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/auth.css">
</head>
<body>
<div class="wrapper">
    <main class="main">
        <form action="/login" class="main__form" method="post">
            <h1 class="main__form__title">Авторизация</h1>
            <#if activateMessage?if_exists != "">
                <div class="main__form__input-group">
                    <small class="main__form__message <#if activateMessageType?if_exists != "">${activateMessageType}</#if>">${activateMessage}</small>
                </div>
            </#if>
            <div class="main__form__input-group">
                <label id="label_name" class="main__form__label" for="name">Логин</label>
                <input class="main__form__input" id="name" type="text" name="name" required>
            </div>
            <div class="main__form__input-group">
                <label id="label_password" class="main__form__label" for="password">Пароль</label>
                <div class="main__form__input__row password-row">
                    <input class="main__form__input" id="password" type="password" name="password" required>
                    <a href="/password/forgot" class="main__form__forget__password"><i class="fa-solid fa-triangle-exclamation"></i></a>
                    <i id="password-toggle" class="fa-regular fa-eye password-toggle"></i>
                </div>
            </div>
            <#if error != "">${error}</#if>
            <input class="main__form__input input-submit" id="submit" type="submit" value="Войти">
            <div class="main__form__alternative">
                <small class="main__form__alternative__text">У вас нет аккаунта?
                    <a class="main__form__alternative__link" href="/signup">Создать</a>
                </small>
            </div>
        </form>
    </main>
</div>
</body>
<script src="/scripts/login.js"></script>
</html>