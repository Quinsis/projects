<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Редактирование матча</title>
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
            <h1>Редактирование матча</h1>
            <a href="/tournament/${tournament.getId()}/#playOff"><h2>Назад</h2></a>
        </div>
        <div class="edit_match">
            <form action="/tournament/${tournament.getId()}/playOff/${playOffStage.getId()}/match/${match.getTeamOwner().getStat().getMatchId()}/edit/${match.getTeamOwner().getStat().getId()}/${match.getTeamGuest().getStat().getId()}"
                  method="post">
                <div class="match">
                    <div class="game_info">
                        <a href="/team/${match.getTeamOwner().getTeam().getId()}" class="owner">
                            <h3>${match.getTeamOwner().getTeam().getName()}</h3>
                            <img src="${match.teamOwner.getTeam().getPhoto()}"
                                 alt="${match.getTeamOwner().getTeam().getName()}">
                        </a>
                        <div class="score">
                            <input class="score_input" type="text" name="goals"
                                   value="${match.getTeamOwner().stat.goals}" required> - <input class="score_input" type="text"
                                                                                                 name="goals1"
                                                                                                 value="${match.getTeamGuest().stat.goals}" required></h2>
                        </div>
                        <a href="/team/${match.getTeamGuest().getTeam().getId()}" class="guest">
                            <img src="${match.getTeamGuest().getTeam().getPhoto()}"
                                 alt="${match.getTeamGuest().getTeam().getName()}">
                            <h3>${match.getTeamGuest().getTeam().getName()}</h3>
                        </a>
                    </div>
                    <p class="date">Дата проведения: ${match.date}</p>
                    <ul class="stats">
                        <li class="stat">
                            <input class="stat_owner" type="text" name="totalAttempts"
                                   value="${match.getTeamOwner().stat.getTotalAttempts()}" required>
                            <p class="stat_name">Удары по воротам</p>
                            <input class="stat_guest" type="text" name="totalAttempts1"
                                   value="${match.getTeamGuest().stat.getTotalAttempts()}" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="goalAttempts"
                                   value="${match.getTeamOwner().stat.getGoalAttempts()}" required>
                            <p class="stat_name">Удары в створ</p>
                            <input class="stat_guest" type="text" name="goalAttempts1"
                                   value="${match.getTeamGuest().stat.getGoalAttempts()}" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="possession"
                                   value="${match.getTeamOwner().stat.getPossession()}" required>
                            <p class="stat_name">Владение мячом</p>
                            <input class="stat_guest" type="text" name="possession1"
                                   value="${match.getTeamGuest().stat.getPossession()}" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="passes"
                                   value="${match.getTeamOwner().stat.getPasses()}" required>
                            <p class="stat_name">Пасы</p>
                            <input class="stat_guest" type="text" name="passes1"
                                   value="${match.getTeamGuest().stat.getPasses()}" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="passesAccuracy"
                                   value="${match.getTeamOwner().stat.getPassesAccuracy()}" required>
                            <p class="stat_name">Точность пасов</p>
                            <input class="stat_guest" type="text" name="passesAccuracy1"
                                   value="${match.getTeamGuest().stat.getPassesAccuracy()}" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="fouls"
                                   value="${match.getTeamOwner().stat.getFouls()}" required>
                            <p class="stat_name">Фолы</p>
                            <input class="stat_guest" type="text" name="fouls1"
                                   value="${match.getTeamGuest().stat.getFouls()}" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="yellowCards"
                                   value="${match.getTeamOwner().stat.getYellowCards()}" required>
                            <p class="stat_name">Жёлтые карточки</p>
                            <input class="stat_guest" type="text" name="yellowCards1"
                                   value="${match.getTeamGuest().stat.getYellowCards()}" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="redCards"
                                   value="${match.getTeamOwner().stat.getRedCards()}" required>
                            <p class="stat_name">Красные карточки</p>
                            <input class="stat_guest" type="text" name="redCards1"
                                   value="${match.getTeamGuest().stat.getRedCards()}" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="offsides"
                                   value="${match.getTeamOwner().stat.getOffsides()}" required>
                            <p class="stat_name">Офсайды</p>
                            <input class="stat_guest" type="text" name="offsides1"
                                   value="${match.getTeamGuest().stat.getOffsides()}" required>
                        </li>
                        <li class="stat">
                            <input class="stat_owner" type="text" name="corners"
                                   value="${match.getTeamOwner().stat.getCorners()}" required>
                            <p class="stat_name">Угловые</p>
                            <input class="stat_guest" type="text" name="corners1"
                                   value="${match.getTeamGuest().stat.getCorners()}" required>
                        </li>
                    </ul>
                    <input class="edit_button" type="submit" value="Подтвердить изменения">
                </div>
            </form>
        </div>
    </div>
</main>
</body>
</html>