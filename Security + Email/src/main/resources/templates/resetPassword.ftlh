<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Сброс пароля</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/auth.css">
</head>
<body>
<div class="wrapper">
    <main class="main">
        <form action="/password/reset?code=${code}" class="main__form" method="post">
            <h1 class="main__form__title">Изменение пароля</h1>
            <div class="main__form__input-group">
                <label id="label_password" class="main__form__label" for="password">Пароль</label>
                <div class="main__form__input__row password-row">
                    <input class="main__form__input" id="password" type="password" name="password" required>
                    <small class="main__form__tips__generate"><i class="fa-solid fa-rotate"></i></small>
                    <i id="password-toggle" class="fa-regular fa-eye password-toggle"></i>
                </div>
            </div>
            <div class="main__form__input-group">
                <label id="label_confirm-password" class="main__form__label" for="confirm-password">Подтверждение пароля</label>
                <div class="main__form__input__row password-row">
                    <input class="main__form__input" id="confirm-password" type="password" name="confirm-password" required>
                    <i id="confirm-password-toggle" class="fa-regular fa-eye password-toggle"></i>
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
            <input class="main__form__input input-submit" id="submit" type="submit" value="Изменить пароль">
        </form>
    </main>
</div>
<script src="/scripts/resetPassword.js"></script>
</body>
</html>