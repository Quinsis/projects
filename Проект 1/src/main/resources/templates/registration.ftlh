<!DOCTYPE html>
<html lang="ru">
<head>
    <link href="/static/css/style.css" rel="stylesheet">
    <title>Регистрация</title>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            min-width: 320px;
            font-family: 'Champions Regular';
            background: #1D2033 !important;
        }

        h2 {
            text-align: center;
        }

        .container {
            padding: 0px 10px;
            max-width: 1140px;
            margin: 0 auto;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .formGroup {
            display: flex;
            flex-direction: column;
            justify-content: center;
            gap: 15px;
            height: 100vh;
        }

        .loginForm {
            width: 100%;
            padding: 15px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            color: white;
            gap: 45px;
        }

        .loginForm input {
            font-family: 'Champions Regular' !important;
            min-width: 370px;
            padding: 13px 10px;
            border: 2px solid #616370;
            border-radius: 7px;
            background: #141824;
            color: white;
        }

        .loginForm input:focus {
            outline-width: 2px;
            outline-color: white;
            box-shadow: 0px 0px 10px rgba(255, 255, 255, 70%);
        }

        .inputGroup {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 10px;
            width: 100%;
        }

        .input {
            display: flex;
            flex-direction: column;
            gap: 7px;
            width: 100%;
        }

        input::-webkit-input-placeholder {
            color: transparent;
        }

        input::-moz-placeholder {
            color: transparent;
        }

        input:-moz-placeholder {
            color: transparent;
        }

        input:-ms-input-placeholder {
            color: transparent;
        }

        input:focus::-webkit-input-placeholder {
            color: #616370;
        }

        input:focus::-moz-placeholder {
            color: #616370;
        }

        input:focus:-moz-placeholder {
            color: #616370;
        }

        input:focus:-ms-input-placeholder {
            color: #616370;
        }

        .input label {
            font-family: 'Champions Regular' !important;
            letter-spacing: 1px;
        }

        .button {
            font-family: 'Champions Regular' !important;
            background: #235FE3 !important;
            color: white;
            font-size: 20px;
            cursor: pointer;
            transition: 0.2s;
            border: none !important;
        }

        .button:hover {
            background: #4075ea !important;
        }


        .login {
            text-decoration: none;
            color: #6888D5;
        }

        .login:hover {
            text-decoration: underline;
        }

    </style>
</head>
<body>
<main>
    <div class="container">
        <div class="formGroup">
            <form class="loginForm" action="/registration" method="post">
                <h2>Создание учётной записи</h2>
                <div class="inputGroup">
                    <div class="input">
                        <label for="name">Имя пользователя</label>
                        <input type="text" id="name" name="name" placeholder="Введите имя пользователя" required>
                    </div>
                    <div class="input">
                        <label for="email">Электронная почта</label>
                        <input type="email" id="email" name="email" placeholder="Введите эл. почту" required>
                    </div>
                    <div class="input">
                        <label for="phone">Телефон</label>
                        <input type="text" id="phone" name="phone" placeholder="Введите телефон" required>
                    </div>
                    <div class="input">
                        <label for="name">Пароль</label>
                        <input type="password" id="password" name="password" placeholder="Введите пароль" required>
                    </div>
                    <#if errorMessage??>
                        <a>${errorMessage}</a>
                    </#if>
                </div>
                <input class="button" type="submit" value="Зарегистрироваться">
                <a class="login" href="/login">Уже зарегистрированы?</a>
            </form>
        </div>
    </div>
</main>
</body>
</html>