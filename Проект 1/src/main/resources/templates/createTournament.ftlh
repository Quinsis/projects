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
        <form class="create_player" action="/tournament/create" method="post">
            <h2>Добавление соревнования УЕФА</h2>
            <div class="inputGroup">
                <div class="input">
                    <label for="name">Название соревнования</label>
                    <input type="text" id="name" name="name" placeholder="Введите название" required>
                </div>
                <div class="input">
                    <label for="photo">Загрузите логотип соревнования</label>
                    <input type="file" id="photo" name="photo" placeholder="Логотип">
                </div>
                <div class="input">
                    <label for="date_begin">Дата начала проведения</label>
                    <input type="text" id="date_begin" name="date_begin" placeholder="Введите дату" required>
                </div>
                <div class="input">
                    <label for="date_end">Дата конца проведения</label>
                    <input type="text" id="date_end" name="date_end" placeholder="Введите дату" required>
                </div>
            </div>
            <input class="button" type="submit" value="Добавить соревнование">
        </form>
    </div>
</main>
</body>
</html>