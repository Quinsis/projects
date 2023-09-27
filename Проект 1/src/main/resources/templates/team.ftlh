<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
    <link rel="stylesheet" href="/static/css/group.css">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/css/player.css">
    <link rel="stylesheet" href="/static/css/team.css">
    <link rel="icon" type="image/x-icon" href="/static/img/uefa.ico">
    <title>Чемпионаты УЕФА | Команды | ${team.getName()}</title>
</head>
<body>
<header class="header">
    <div class="container">
        <div class="navbar">
            <a href="/" onclick="" class="title">Футбольные соревнования</a>
            <div class="dropdown">
                <h3>${user.name}<span></span></h3>
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
        <div class="team">
            <div class="team_info">
                <div class="team_logo">
                    <img src="${team.getPhoto()}" alt="">
                    <div class="team_about">
                        <h1>${team.getName()}</h1>
                        <div class="match_history">
                            <h4>Последние 5 матчей: </h4>
                            <ul>
                                <#list matchHistory.matches as match>
                                    <#if match.getStatus().toString() == "WON">
                                        <b class="matchWon">В</b>
                                    </#if>
                                    <#if match.getStatus().toString() == "DRAW">
                                        <b class="matchDraw">Н</b>
                                    </#if>
                                    <#if match.getStatus().toString() == "LOSS">
                                        <b class="matchLoss">П</b>
                                    </#if>
                                </#list>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="matches">
                    <h1>История матчей</h1>
                    <#list history as match>
                        <div class="match">
                            <div class="match_nav">
                                <#if match.getPlayOffStage().getId() != -1>
                                    <p>${match.getTournament().getName()} / Плей-офф (${match.getPlayOffStage().getName()})</p>
                                <#else>
                                    <p>${match.getTournament().getName()} / Групповой этап</p>
                                </#if>
                                <i id="${match.hashCode()}" class="expand fa-solid fa-chevron-down"></i>
                            </div>
                            <div class="game_info">
                                <a href="/team/${match.getTeamOwner().getTeam().getId()}"
                                   class="owner">
                                    <h3>${match.getTeamOwner().getTeam().getName()}</h3>
                                    <img src="${match.teamOwner.getTeam().getPhoto()}"
                                         alt="${match.getTeamOwner().getTeam().getName()}">
                                </a>
                                <div class="score">
                                    <h2>${match.getTeamOwner().getStat().goals} - ${match.getTeamGuest().getStat().goals}</h2>
                                </div>
                                <a href="/team/${match.getTeamGuest().getTeam().getId()}"
                                   class="guest">
                                    <img src="${match.getTeamGuest().getTeam().getPhoto()}"
                                         alt="${match.getTeamGuest().getTeam().getName()}">
                                    <h3>${match.getTeamGuest().getTeam().getName()}</h3>
                                </a>
                            </div>
                            <p class="date">Дата проведения: ${match.date}</p>
                            <ul class="stats">
                                <li class="stat">
                                    <h3 class="stat_owner">${match.getTeamOwner().stat.getTotalAttempts()}</h3>
                                    <p class="stat_name">Удары по воротам</p>
                                    <h3 class="stat_guest">${match.getTeamGuest().stat.getTotalAttempts()}</h3>
                                </li>
                                <li class="stat">
                                    <h3 class="stat_owner">${match.getTeamOwner().stat.getGoalAttempts()}</h3>
                                    <p class="stat_name">Удары в створ</p>
                                    <h3 class="stat_guest">${match.getTeamGuest().stat.getGoalAttempts()}</h3>
                                </li>
                                <li class="stat">
                                    <h3 class="stat_owner">${match.getTeamOwner().stat.getPossession()}%</h3>
                                    <p class="stat_name">Владение мячом</p>
                                    <h3 class="stat_guest">${match.getTeamGuest().stat.getPossession()}%</h3>
                                </li>
                                <li class="stat">
                                    <h3 class="stat_owner">${match.getTeamOwner().stat.getPasses()}</h3>
                                    <p class="stat_name">Пасы</p>
                                    <h3 class="stat_guest">${match.getTeamGuest().stat.getPasses()}</h3>
                                </li>
                                <li class="stat">
                                    <h3 class="stat_owner">${match.getTeamOwner().stat.getPassesAccuracy()}%</h3>
                                    <p class="stat_name">Точность пасов</p>
                                    <h3 class="stat_guest">${match.getTeamGuest().stat.getPassesAccuracy()}%</h3>
                                </li>
                                <li class="stat">
                                    <h3 class="stat_owner">${match.getTeamOwner().stat.getFouls()}</h3>
                                    <p class="stat_name">Фолы</p>
                                    <h3 class="stat_guest">${match.getTeamGuest().stat.getFouls()}</h3>
                                </li>
                                <li class="stat">
                                    <h3 class="stat_owner">${match.getTeamOwner().stat.getYellowCards()}</h3>
                                    <p class="stat_name">Жёлтые карточки</p>
                                    <h3 class="stat_guest">${match.getTeamGuest().stat.getYellowCards()}</h3>
                                </li>
                                <li class="stat">
                                    <h3 class="stat_owner">${match.getTeamOwner().stat.getRedCards()}</h3>
                                    <p class="stat_name">Красные карточки</p>
                                    <h3 class="stat_guest">${match.getTeamGuest().stat.getRedCards()}</h3>
                                </li>
                                <li class="stat">
                                    <h3 class="stat_owner">${match.getTeamOwner().stat.getOffsides()}</h3>
                                    <p class="stat_name">Офсайды</p>
                                    <h3 class="stat_guest">${match.getTeamGuest().stat.getOffsides()}</h3>
                                </li>
                                <li class="stat">
                                    <h3 class="stat_owner">${match.getTeamOwner().stat.getCorners()}</h3>
                                    <p class="stat_name">Угловые</p>
                                    <h3 class="stat_guest">${match.getTeamGuest().stat.getCorners()}</h3>
                                </li>
                            </ul>
                        </div>
                    </#list>
                </div>
            </div>
            <div class="players_block">
                <ul class="players_block_header">
                    <li><h1>Игроки</h1></li>
                    <#if user.isAdmin()>
                        <li><a href="/team/${team.getId()}/player/create"><h3>Добавить игрока</h3></a></li>
                    </#if>
                </ul>
                <ul class="players">
                    <#list players as player>
                        <li class="player">
                            <div class="player_preview">
                                <img src="${player.getPerson().getPhoto()}" alt="">
                                <h2>${player.getPerson().getName()} ${player.getPerson().getSurname()}</h2>
                            </div>
                            <div class="player_position">
                                <h4>${player.getPlayerPosition().getName()}</h4>
                                <#if user.isAdmin()>
                                    <a href="/player/${player.getId()}/delete">Удалить игрока</a>
                                </#if>
                            </div>
                        </li>
                    </#list>
                </ul>
            </div>
        </div>
    </div>
</main>
<script>
    let expandButtons = document.getElementsByClassName("expand");
    let match = document.getElementsByClassName("match");
    let stats = document.getElementsByClassName("stats");
    let expanded = [];
    for (let i = 0; i < expandButtons.length; i++) {
        expanded.push(false);
    }

    for (let i = 0; i < expandButtons.length; i++) {
        expandButtons[i].onclick = function () {
            if (expanded[i] === false) {
                document.getElementById(expandButtons[i].id).style.transform = 'rotate(180deg)';
                expanded[i] = true;
                stats[i].style.display = "flex";
                match[i].style.height = "auto";
            } else {
                document.getElementById(expandButtons[i].id).style.transform = 'rotate(0deg)';
                expanded[i] = false;
                stats[i].style.display = "none";
                match[i].style.height = "";
            }
        };
    }
</script>
</body>
</html>