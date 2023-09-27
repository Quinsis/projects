<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Чемпионаты УЕФА</title>
    <link rel="icon" type="image/x-icon" href="/static/img/uefa.ico">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/css/tournament.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
</head>
<body>
<header class="header">
    <div class="container">
        <div class="navbar">
            <a href="/" onclick="" class="title">Футбольные соревнования</a>
            <div class="dropdown">
                <h3>${user.getName()}<span></span></h3>
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
        <div class="tournaments_block">
            <h1>Соревнования</h1>
            <ul class="tournaments">
                <#list tournaments as tournament>
                    <li>
                        <h2>${tournament.name}</h2>
                        <a class="tournament" href="/tournament/${tournament.id}">
                            <img class="tournament-img" src="${tournament.photo}" alt="${tournament.name}">
                            <div class="tournament_info">
                                <div class="date">
                                    <span>Дата проведения: </span>
                                    <p>${tournament.date_begin} / ${tournament.date_end}</p>
                                </div>
                            </div>
                            <#list winners as winner>
                                <#if winner.getTournament().getId() == tournament.getId()>
                                    <ul class="winner">
                                        <img src="${winner.getTeam().getPhoto()}" alt="">
                                        <b id="teamTag">Победитель</b>
                                    </ul>
                                </#if>
                            </#list>
                        </a>
                    </li>
                </#list>
                <#if user.isAdmin()>
                    <a class="tournament-add" href="/tournament/create">
                        <i class="fa-solid fa-circle-plus match-add"></i>
                    </a>
                </#if>
            </ul>
        </div>
    </div>
</main>
</body>
</html>