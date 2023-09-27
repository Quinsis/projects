<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Чемпионаты УЕФА: ${tournament.getName()} | ${group.getName()}</title>
    <link rel="icon" type="image/x-icon" href="/static/img/uefa.ico">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/css/group.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
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
        <div class="group_nav">
            <h1>Информация о группе</h1>
            <#if user.isAdmin()>
                <a href="/tournament/${tournament.getId()}/group/${group.getId()}/createMatch"><h2>Добавить матч</h2>
                </a>
            </#if>
            <a href="/tournament/${tournament.getId()}/#groupStage"><h2>Назад</h2></a>
        </div>
        <div class="group_info">
            <div class="group_header">
                <h3>${group.name}</h3>
                <ul class="stat_list">
                    <li>
                        <h3>И</h3>
                    </li>
                    <li>
                        <h3>В</h3>
                    </li>
                    <li>
                        <h3>Н</h3>
                    </li>
                    <li>
                        <h3>П</h3>
                    </li>
                    <li>
                        <h3>РМ</h3>
                    </li>
                    <li>
                        <h3>О</h3>
                    </li>
                </ul>
            </div>
            <div class="group_main">
                <div class="teams">
                    <#list teams as team>
                        <#if team.getGroup().getId() == group.getId()>
                            <a href="/team/${team.getTeam().getId()}" class="team">
                                <span class="${team.getGroupStats().getQualifiedStatus()}"></span>
                                <div class="preview">
                                    <div class="image">
                                        <img src="${team.getTeam().getPhoto()}"
                                             alt="${team.getTeam().getName()}">
                                    </div>
                                    <h3>${team.getTeam().getName()}</h3>
                                </div>
                                <ul class="stat_list">
                                    <li>
                                        <h3>${team.getGroupStats().getGames()}</h3>
                                    </li>
                                    <li>
                                        <h3>${team.getGroupStats().getWon()}</h3>
                                    </li>
                                    <li>
                                        <h3>${team.getGroupStats().getDraw()}</h3>
                                    </li>
                                    <li>
                                        <h3>${team.getGroupStats().getLoss()}</h3>
                                    </li>
                                    <li>
                                        <h3>${team.getGroupStats().getDiffGoals()}</h3>
                                    </li>
                                    <li>
                                        <h3>${team.getGroupStats().getPoints()}</h3>
                                    </li>
                                </ul>
                            </a>
                        </#if>
                    </#list>
                </div>
            </div>
            <div class="group_tips">
                <p><span class="tip_qualified"></span>Следующий раунд</p>
                <#if user.isAdmin() && teamSize != 4>
                    <p id="addTeam">Добавить команду</p>
                </#if>
                <form action="/tournament/${tournament.getId()}/group/${group.getId()}/addTeams" name="teamList" id="teamList" class="teamList" method="post">
                    <div class="teamList-content">
                        <i id="close" class="fa-solid fa-xmark"></i>
                        <#list allTeams as team>
                            <p><input type="checkbox" name="team[]" value="${team.getTeam().getName()}"/>${team.getTeam().getName()}</p>
                        </#list>
                    </div>
                    <input class="button" type="submit" value="Добавить команды" disabled>
                </form>
            </div>
        </div>
        <h1 class="matches_head">Прошедшие матчы</h1>
        <div class="matches group_matches">
            <#list matches as match>
                <div class="match">
                    <div class="match_nav">
                        <#if user.isAdmin() || user.isOperator()>
                            <a class="edit"
                               href="/tournament/${tournament.getId()}/group/${group.getId()}/match/${match.getTeamOwner().stat.getMatchId()}/edit/">Редактировать</a>
                        </#if>
                        <p>${tournament.getName()} / Групповой этап</p>
                        <i id="${match.hashCode()}" class="expand fa-solid fa-chevron-down"></i>
                    </div>
                    <div class="game_info">
                        <a href="/team/${match.getTeamOwner().getTeam().getId()}" class="owner">
                            <h3>${match.getTeamOwner().getTeam().getName()}</h3>
                            <img src="${match.teamOwner.getTeam().getPhoto()}"
                                 alt="${match.getTeamOwner().getTeam().getName()}">
                        </a>
                        <div class="score">
                            <h2>${match.getTeamOwner().stat.goals} - ${match.getTeamGuest().stat.goals}</h2>
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
    <script>
        let expandButtons = document.getElementsByClassName("expand");
        let match = document.getElementsByClassName("match");
        let stats = document.getElementsByClassName("stats");
        let expanded = [];
        for (let i = 0; i < expandButtons.length; i++) {
            expanded.push(false);
        }
        let form = document.forms[0];

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

        document.getElementById("addTeam").onclick = function () {
            document.getElementsByClassName("header")[0].style.zIndex = "1000";
            if (document.getElementById("teamList").classList.contains("target")) {
                document.getElementById("teamList").classList.remove("target");
            } else {
                document.getElementById("teamList").classList.add("target");
            }
        }

        document.getElementById("close").onclick = function () {
            document.getElementsByClassName("header")[0].style.zIndex = "10000";
            for (let i = 0; i < form.elements.length - 1; i++) {
                form.elements[i].checked = false;
            }
            document.getElementById("teamList").classList.remove("target");
        }

        let checked = 0;
        for (let i = 0; i < form.elements.length - 1; i++) {
            if (form.elements[i].checked) checked++;
        }

        if (checked === 4 - ${teamSize}) {
            for (let i = 0; i < form.elements.length - 1; i++) {
                if (!form.elements[i].checked) {
                    form.elements[i].disabled = true;
                }
            }
        } else {
            for (let i = 0; i < form.elements.length - 1; i++) {
                form.elements[i].disabled = false;
            }
        }

        form.onchange = function() {
            let checked = 0;
            for (let i = 0; i < form.elements.length - 1; i++) {
                if (form.elements[i].checked) checked++;
            }

            if (checked === 4 - ${teamSize}) {
                for (let i = 0; i < form.elements.length - 1; i++) {
                    if (!form.elements[i].checked) {
                        form.elements[i].disabled = true;
                    }
                }
            } else {
                for (let i = 0; i < form.elements.length - 1; i++) {
                    form.elements[i].disabled = false;
                }
            }

            if (checked > 0) {
                form.elements[form.elements.length - 1].disabled = false;
            }

            if (checked == 0) {
                form.elements[form.elements.length - 1].disabled = true;
            }
        };
    </script>
</main>
</body>
</html>