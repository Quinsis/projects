<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Добавление игрока</title>
    <link rel="icon" type="image/x-icon" href="/static/img/uefa.ico">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/css/player.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
    <link rel="stylesheet" href="/static/css/editMatch.css">
</head>
<body>
<header class="header">
    <div class="container">
        <div class="navbar">
            <a href="/" onclick="" class="title">Футбольные соревнования</a>
            <div class="dropdown">
                <h3>${user.name}</h3>
                <div class="dropdown-options">
                    <a href="/profile"><i class="fa-solid fa-user"></i>Личный кабинет</a>
                    <#if user.isAdmin()>
                        <a href="/admin"><i class="fa-solid fa-screwdriver-wrench"></i>Панель администратора</a>
                    </#if>
                    <a href="/logout"><i class="fa-solid fa-arrow-right-from-bracket"></i>Выйти</a>
                </div>
            </div>
        </div>
    </div>
</header>
<main class="creationPage">
    <div class="formGroup">
        <form class="create_player" action="/team/${team.getId()}/createPlayer" method="post">
            <h2>Добавление игрока в команду ${team.getName()}</h2>
            <div class="inputGroup">
                <div class="input">
                    <label for="name">Имя</label>
                    <input type="text" id="name" name="name" placeholder="Введите имя" required>
                </div>
                <div class="input">
                    <label for="surname">Фамилия</label>
                    <input type="text" id="surname" name="surname" placeholder="Введите фамилию" required>
                </div>
                <div class="input">
                    <label for="patronymic">Отчество (при наличии)</label>
                    <input type="text" id="patronymic" name="patronymic" placeholder="Введите отчество">
                </div>
                <div class="input">
                    <label>Выберите позицию</label>
                    <select class="select" name="position">
                        <#list positions as position>
                            <option value="${position.getName()}">${position.getName()}</option>
                        </#list>
                    </select>
                </div>
                <div class="input">
                    <label>Выберите страну</label>
                    <select class="select" name="country">
                        <#list countries as country>
                            <option value="${country.getName()}">${country.getName()}</option>
                        </#list>
                    </select>
                </div>
                <div class="input">
                    <label for="photo">Загрузите фотографию</label>
                    <input type="file" id="photo" name="photo" placeholder="Фотография">
                </div>
            </div>
            <input class="button" type="submit" value="Добавить игрока">
        </form>
    </div>
</main>
</body>
</html>