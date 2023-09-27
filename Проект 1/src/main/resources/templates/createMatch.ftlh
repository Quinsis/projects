<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Создание матча</title>
    <link rel="icon" type="image/x-icon" href="/static/img/uefa.ico">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/css/group.css">
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
<main>
    <div class="container">
        <div class="group_nav">
            <h1>Создание матча</h1>
            <a href="/tournament/${tournament.getId()}/group/${group.getId()}"><h2>Назад</h2></a>
        </div>
        <div class="create_match">
            <form action="/group/${group.getId()}/createMatch" method="post">
                <div class="match">
                    <div class="game_info">
                        <select class="teamSelection" name="teamOwnerSelect">
                            <#list teams as team>
                                <option class="teamOption" name="teamOwnerOption">
                                    <h3>${team.getTeam().getName()}</h3>
                                </option>
                            </#list>
                        </select>
                        <div class="score">
                            <input class="score_input" type="text" name="goals" required> - <input class="score_input"
                                                                                          type="text"
                                                                                          name="goals1" required></h2>
                        </div>
                        <select class="teamSelection" name="teamGuestSelect">
                            <#list teams as team>
                                <option class="teamOption" name="teamGuestOption">
                                    <h3>${team.getTeam().getName()}</h3>
                                </option>
                            </#list>
                        </select>
                    </div>
                    <p class="date">Дата проведения: <input class="matchDate" type="text" name="matchDate" required></p>
                    <ul class="stats">
                        <li class="stat">
                            <input class="stat_owner" type="text" name="totalAttempts" required>
                            <p class="stat_name">Удары по воротам</p>
                            <input class="stat_guest" type="text" name="totalAttempts1" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="goalAttempts" required>
                            <p class="stat_name">Удары в створ</p>
                            <input class="stat_guest" type="text" name="goalAttempts1" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="possession" required>
                            <p class="stat_name">Владение мячом</p>
                            <input class="stat_guest" type="text" name="possession1" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="passes" required>
                            <p class="stat_name">Пасы</p>
                            <input class="stat_guest" type="text" name="passes1" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="passesAccuracy" required>
                            <p class="stat_name">Точность пасов</p>
                            <input class="stat_guest" type="text" name="passesAccuracy1" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="fouls" required>
                            <p class="stat_name">Фолы</p>
                            <input class="stat_guest" type="text" name="fouls1" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="yellowCards" required>
                            <p class="stat_name">Жёлтые карточки</p>
                            <input class="stat_guest" type="text" name="yellowCards1" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="redCards" required>
                            <p class="stat_name">Красные карточки</p>
                            <input class="stat_guest" type="text" name="redCards1" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="offsides" required>
                            <p class="stat_name">Офсайды</p>
                            <input class="stat_guest" type="text" name="offsides1" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="corners" required>
                            <p class="stat_name">Угловые</p>
                            <input class="stat_guest" type="text" name="corners1" required>
                        </li>
                    </ul>
                    <input class="edit_button" type="submit" value="Создать матч">
                </div>
            </form>
        </div>
    </div>
</main>
</body>
</html>