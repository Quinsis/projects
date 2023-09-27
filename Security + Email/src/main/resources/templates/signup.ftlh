<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Регистрация</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/auth.css">
</head>
<body>
<div class="wrapper">
    <main class="main">
        <form action="/signup" class="main__form" method="post">
            <h1 class="main__form__title">Регистрация</h1>
            <div class="main__form__input-group <#if errorMessage="Логин уже используется">input-error</#if>">
                <label id="label_name" class="main__form__label" for="name">Логин</label>
                <input class="main__form__input" id="name" type="text" name="name"
                       <#if errorMessage != "">value="${account.name}"</#if> required>
                <#if errorMessage == "Логин уже используется">
                    <small class="main__form__error"><i class="fa-solid fa-circle-exclamation"></i> ${errorMessage}</small>
                </#if>
            </div>
            <div class="main__form__input-group <#if errorMessage="Почта уже используется">input-error</#if>">
                <label id="label_email" class="main__form__label" for="email">Почта</label>
                <input class="main__form__input" id="email" type="email" name="email"
                       <#if errorMessage != "">value="${account.email}"</#if> required>
                <#if errorMessage == "Почта уже используется">
                    <small class="main__form__error"><i class="fa-solid fa-circle-exclamation"></i> ${errorMessage}</small>
                </#if>
            </div>
            <div class="main__form__input-group">
                <label id="label_password" class="main__form__label" for="password">Пароль</label>
                <div class="main__form__input__row password-row">
                    <input class="main__form__input" id="password" type="password" name="password" required>
                    <small class="main__form__tips__generate"><i class="fa-solid fa-rotate"></i></small>
                    <i id="password-toggle" class="fa-regular fa-eye password-toggle"></i>
                </div>
            </div>
            <div class="main__form__tips">
                <small class="main__form__tips__text">Ваш пароль должен содержать:</small>
                <ul class="main__form__tips__list">
                    <li class="main__form__tips__item">
                        <small class="main__form__tips__item__small">Хотя бы 16 символов</small>
                    </li>
                    <li class="main__form__tips__item">
                        <small class="main__form__tips__item__small">Прописные и строчные латинские буквы</small>
                    </li>
                    <li class="main__form__tips__item">
                        <small class="main__form__tips__item__small">Цифры и символы: ! " # $ % & и т.д.</small>
                    </li>
                </ul>
            </div>
            <div class="main__form__input-group <#if errorMessage="Телефон уже используется" || errorMessage == "Неверный формат телефона">input-error</#if>">
                <label id="label_phone" class="main__form__label label-phone" for="phone">Телефон</label>
                <div class="main__form__input__row">
                    <small class="main__form__input__phone__code">+7</small>
                    <input class="main__form__input" id="phone" type="text" name="phone"
                           <#if errorMessage != "">value="${account.phone}"</#if> required>
                </div>
                <#if errorMessage == "Телефон уже используется" || errorMessage == "Неверный формат телефона">
                    <small class="main__form__error error-phone"><i class="fa-solid fa-circle-exclamation"></i> ${errorMessage}</small>
                </#if>
            </div>
            <input class="main__form__input input-submit disabled" id="submit" type="submit" value="Зарегистрироваться" disabled>
            <div class="main__form__alternative">
                <small class="main__form__alternative__text">У вас есть аккаунт?
                    <a class="main__form__alternative__link" href="/login">Войти</a>
                </small>
            </div>
        </form>
    </main>
</div>
<script src="/scripts/signup.js"></script>
</body>
</html>