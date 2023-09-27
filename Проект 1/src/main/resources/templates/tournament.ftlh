<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Чемпионаты УЕФА: ${tournament.getName()}</title>
    <link rel="icon" type="image/x-icon" href="/static/img/uefa.ico">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/css/group.css">
    <link rel="stylesheet" href="/static/css/tournament.css">
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
        <div id="groupStage" class="groups_nav">
            <h1>${tournament.getName()} / Групповой этап</h1>
            <a href="/"><h2>Назад</h2></a>
        </div>
        <div class="groups">
            <#list groups as group>
                <div id="group${group.getId()}" class="group">
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
                            <#list teamsInfo as teamInfo>
                                <#if teamInfo.getGroup().getId() == group.getId()>
                                    <a href="/team/${teamInfo.getTeam().getId()}" class="team">
                                        <span class="${teamInfo.getGroupStats().getQualifiedStatus()}"
                                              id="${teamInfo.getGroupStats().getQualifiedStatus()}"></span>
                                        <div class="preview">
                                            <div class="image">
                                                <img src="${teamInfo.getTeam().getPhoto()}"
                                                     alt="${teamInfo.getTeam().getName()}">
                                            </div>
                                            <h3>${teamInfo.getTeam().getName()}</h3>
                                        </div>
                                        <ul class="stat_list">
                                            <li>
                                                <h3>${teamInfo.getGroupStats().getGames()}</h3>
                                            </li>
                                            <li>
                                                <h3>${teamInfo.getGroupStats().getWon()}</h3>
                                            </li>
                                            <li>
                                                <h3>${teamInfo.getGroupStats().getDraw()}</h3>
                                            </li>
                                            <li>
                                                <h3>${teamInfo.getGroupStats().getLoss()}</h3>
                                            </li>
                                            <li>
                                                <h3>${teamInfo.getGroupStats().getDiffGoals()}</h3>
                                            </li>
                                            <li>
                                                <h3>${teamInfo.getGroupStats().getPoints()}</h3>
                                            </li>
                                        </ul>
                                    </a>
                                </#if>
                            </#list>
                        </div>
                    </div>
                    <div class="group_tips">
                        <ul>
                            <li>
                                <p><span class="tip_qualified"></span>Следующий раунд</p>
                            </li>
                        </ul>
                        <a href="/tournament/${id}/group/${group.getId()}">Подробная таблица</a>
                    </div>
                </div>
            </#list>
        </div>
        <div id="playOff" class="playoff_nav">
            <h1>${tournament.getName()} / Плей-офф</h1>
        </div>
        <div class="bracket">
            <div class="bracket_container">
                <#list playOffStages as round>
                    <div class="round"
                         <#if round.getName() == "1/8 Финала">id="odd"</#if>
                            <#if round.getName() == "1/4 Финала">id="even"</#if>
                            <#if round.getName() == "Полуфинал">id="odd"</#if>
                            <#if round.getName() == "Финал">id="even"</#if>
                    >
                        <ul class="round-nav">
                            <h3>${round.getName()}</h3>
                            <#if round.getName() != "Финал">
                                <ul class="tours">
                                    <p>Матч</p>
                                    <a class="tour" id="tour1${round.id}">1</a>
                                    <a class="tour" id="tour2${round.id}">2</a>
                                </ul>
                            </#if>
                        </ul>
                        <#list playOffMatchesTourOne as roundMatch>
                            <#if roundMatch.getPlayOffStage() == round && roundMatch.getTour() == 1>
                                <div class="matchUp-container matchUp-tourOne matchUp-played">
                                    <div class="matchUp-options">
                                        <#if user.isAdmin() || user.isOperator()><i class="edit fa-regular fa-pen-to-square"></i></#if>
                                        <i class="edit fa-solid fa-chart-bar"></i>
                                        <#if user.isAdmin() || user.isOperator()><a href="/tournament/${tournament.getId()}/playOff/${round.getId()}/match/${roundMatch.getMatch().getId()}/edit">Редактировать</a></#if>
                                        <a class="show">Детали матча</a>
                                    </div>
                                    <div class="matchUp">
                                        <a href="/team/${roundMatch.getTeamOwner().getTeam().getId()}" class="matchUp_team">
                                            <div class="team_tag">
                                                <img src="${roundMatch.getTeamOwner().getTeam().getPhoto()}" alt="">
                                                <p>${roundMatch.getTeamOwner().getTeam().getName()}</p>
                                            </div>
                                            <p>${roundMatch.getTeamOwner().getStat().getGoals()}</p>
                                        </a>
                                        <a href="/team/${roundMatch.getTeamGuest().getTeam().getId()}" class="matchUp_team">
                                            <div class="team_tag">
                                                <img src="${roundMatch.getTeamGuest().getTeam().getPhoto()}" alt="">
                                                <p>${roundMatch.getTeamGuest().getTeam().getName()}</p>
                                            </div>
                                            <p>${roundMatch.getTeamGuest().getStat().getGoals()}</p>
                                        </a>
                                    </div>
                                </div>
                                <div id="match${roundMatch.getMatch().getId()}" class="match">
                                    <div class="match-content">
                                        <div class="match_nav">
                                            <span></span>
                                            <a class="close"><i class="fa-solid fa-xmark"></i></a>
                                        </div>
                                        <div class="game_info">
                                            <a href="/team/${roundMatch.getTeamOwner().getTeam().getId()}" class="owner">
                                                <img src="${roundMatch.teamOwner.getTeam().getPhoto()}"
                                                     alt="${roundMatch.getTeamOwner().getTeam().getName()}">
                                            </a>
                                            <div class="score">
                                                <h2>${roundMatch.getTeamOwner().stat.goals} - ${roundMatch.getTeamGuest().stat.goals}</h2>
                                            </div>
                                            <a href="/team/${roundMatch.getTeamGuest().getTeam().getId()}" class="guest">
                                                <img src="${roundMatch.getTeamGuest().getTeam().getPhoto()}"
                                                     alt="${roundMatch.getTeamGuest().getTeam().getName()}">
                                            </a>
                                        </div>
                                        <p class="date">Дата проведения: ${roundMatch.date}</p>
                                        <ul class="stats">
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getTotalAttempts()}</h3>
                                                <p class="stat_name">Удары по воротам</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getTotalAttempts()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getGoalAttempts()}</h3>
                                                <p class="stat_name">Удары в створ</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getGoalAttempts()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getPossession()}%</h3>
                                                <p class="stat_name">Владение мячом</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getPossession()}%</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getPasses()}</h3>
                                                <p class="stat_name">Пасы</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getPasses()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getPassesAccuracy()}%</h3>
                                                <p class="stat_name">Точность пасов</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getPassesAccuracy()}%</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getFouls()}</h3>
                                                <p class="stat_name">Фолы</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getFouls()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getYellowCards()}</h3>
                                                <p class="stat_name">Жёлтые карточки</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getYellowCards()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getRedCards()}</h3>
                                                <p class="stat_name">Красные карточки</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getRedCards()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getOffsides()}</h3>
                                                <p class="stat_name">Офсайды</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getOffsides()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getCorners()}</h3>
                                                <p class="stat_name">Угловые</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getCorners()}</h3>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </#if>
                        </#list>
                        <#list playOffMatchesTourTwo as roundMatch>
                            <#if roundMatch.getPlayOffStage() == round && roundMatch.getTour() == 2>
                                <div class="matchUp-container matchUp-tourTwo matchUp-played">
                                    <div class="matchUp-options">
                                        <#if user.isAdmin() || user.isOperator()><i class="edit fa-regular fa-pen-to-square"></i></#if>
                                        <i class="edit fa-solid fa-chart-bar"></i>
                                        <#if user.isAdmin() || user.isOperator()><a href="/tournament/${tournament.getId()}/playOff/${round.getId()}/match/${roundMatch.getMatch().getId()}/edit">Редактировать</a></#if>
                                        <a class="show">Детали матча</a>
                                    </div>
                                    <div class="matchUp">
                                        <a href="/team/${roundMatch.getTeamOwner().getTeam().getId()}" class="matchUp_team">
                                            <div class="team_tag">
                                                <img src="${roundMatch.getTeamOwner().getTeam().getPhoto()}" alt="">
                                                <p>${roundMatch.getTeamOwner().getTeam().getName()}</p>
                                            </div>
                                            <p>${roundMatch.getTeamOwner().getStat().getGoals()}</p>
                                        </a>
                                        <a href="/team/${roundMatch.getTeamGuest().getTeam().getId()}" class="matchUp_team">
                                            <div class="team_tag">
                                                <img src="${roundMatch.getTeamGuest().getTeam().getPhoto()}" alt="">
                                                <p>${roundMatch.getTeamGuest().getTeam().getName()}</p>
                                            </div>
                                            <p>${roundMatch.getTeamGuest().getStat().getGoals()}</p>
                                        </a>
                                    </div>
                                </div>
                                <div id="match${roundMatch.getMatch().getId()}" class="match">
                                    <div class="match-content">
                                        <div class="match_nav">
                                            <span></span>
                                            <a class="close"><i class="fa-solid fa-xmark"></i></a>
                                        </div>
                                        <div class="game_info">
                                            <a href="/team/${roundMatch.getTeamOwner().getTeam().getId()}" class="owner">
                                                <img src="${roundMatch.teamOwner.getTeam().getPhoto()}"
                                                     alt="${roundMatch.getTeamOwner().getTeam().getName()}">
                                            </a>
                                            <div class="score">
                                                <h2>${roundMatch.getTeamOwner().stat.goals} - ${roundMatch.getTeamGuest().stat.goals}</h2>
                                            </div>
                                            <a href="/team/${roundMatch.getTeamGuest().getTeam().getId()}" class="guest">
                                                <img src="${roundMatch.getTeamGuest().getTeam().getPhoto()}"
                                                     alt="${roundMatch.getTeamGuest().getTeam().getName()}">
                                            </a>
                                        </div>
                                        <p class="date">Дата проведения: ${roundMatch.date}</p>
                                        <ul class="stats">
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getTotalAttempts()}</h3>
                                                <p class="stat_name">Удары по воротам</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getTotalAttempts()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getGoalAttempts()}</h3>
                                                <p class="stat_name">Удары в створ</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getGoalAttempts()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getPossession()}%</h3>
                                                <p class="stat_name">Владение мячом</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getPossession()}%</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getPasses()}</h3>
                                                <p class="stat_name">Пасы</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getPasses()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getPassesAccuracy()}%</h3>
                                                <p class="stat_name">Точность пасов</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getPassesAccuracy()}%</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getFouls()}</h3>
                                                <p class="stat_name">Фолы</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getFouls()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getYellowCards()}</h3>
                                                <p class="stat_name">Жёлтые карточки</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getYellowCards()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getRedCards()}</h3>
                                                <p class="stat_name">Красные карточки</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getRedCards()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getOffsides()}</h3>
                                                <p class="stat_name">Офсайды</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getOffsides()}</h3>
                                            </li>
                                            <li class="stat">
                                                <h3 class="stat_owner">${roundMatch.getTeamOwner().stat.getCorners()}</h3>
                                                <p class="stat_name">Угловые</p>
                                                <h3 class="stat_guest">${roundMatch.getTeamGuest().stat.getCorners()}</h3>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </#if>
                        </#list>
                        <#switch round.getName()>
                            <#case "1/8 Финала">
                                <#list oneOfEighthMatchesTourOne as oneOfEighthMatch>
                                    <a <#if user.isAdmin()>href="/tournament/${tournament.getId()}/playOff/${round.getId()}/createPlayOffMatch/tour/1"</#if> class="matchUp matchUp-tourOne matchUp-unplayed">
                                        <p class="matchUp_team"></p>
                                        <#if user.isAdmin()><i class="fa-solid fa-circle-plus match-add"></i></#if>
                                        <p class="matchUp_team"></p>
                                    </a>
                                </#list>
                                <#break>
                            <#case "1/4 Финала">
                                <#list oneOfFourthMatchesTourOne as oneOfFourthMatch>
                                    <a <#if user.isAdmin()>href="/tournament/${tournament.getId()}/playOff/${round.getId()}/createPlayOffMatch/tour/1"</#if> class="matchUp matchUp-tourOne matchUp-unplayed">
                                        <p class="matchUp_team"></p>
                                        <#if user.isAdmin()><i class="fa-solid fa-circle-plus match-add"></i></#if>
                                        <p class="matchUp_team"></p>
                                    </a>
                                </#list>
                                <#break>
                            <#case "Полуфинал">
                                <#list semiFinalMatchesTourOne as semiFinalMatch>
                                    <a <#if user.isAdmin()>href="/tournament/${tournament.getId()}/playOff/${round.getId()}/createPlayOffMatch/tour/1"</#if> class="matchUp matchUp-tourOne matchUp-unplayed">
                                        <p class="matchUp_team"></p>
                                        <#if user.isAdmin()><i class="fa-solid fa-circle-plus match-add"></i></#if>
                                        <p class="matchUp_team"></p>
                                    </a>
                                </#list>
                                <#break>
                            <#default>
                                <#break>
                        </#switch>
                        <#switch round.getName()>
                            <#case "1/8 Финала">
                                <#list oneOfEighthMatchesTourTwo as oneOfEighthMatch>
                                    <a <#if user.isAdmin()>href="/tournament/${tournament.getId()}/playOff/${round.getId()}/createPlayOffMatch/tour/2"</#if> class="matchUp matchUp-tourTwo matchUp-unplayed">
                                        <p class="matchUp_team"></p>
                                        <#if user.isAdmin()><i class="fa-solid fa-circle-plus match-add"></i></#if>
                                        <p class="matchUp_team"></p>
                                    </a>
                                </#list>
                                <#break>
                            <#case "1/4 Финала">
                                <#list oneOfFourthMatchesTourTwo as oneOfFourthMatch>
                                    <a <#if user.isAdmin()>href="/tournament/${tournament.getId()}/playOff/${round.getId()}/createPlayOffMatch/tour/2"</#if> class="matchUp matchUp-tourTwo matchUp-unplayed">
                                        <p class="matchUp_team"></p>
                                        <#if user.isAdmin()><i class="fa-solid fa-circle-plus match-add"></i></#if>
                                        <p class="matchUp_team"></p>
                                    </a>
                                </#list>
                                <#break>
                            <#case "Полуфинал">
                                <#list semiFinalMatchesTourTwo as semiFinalMatch>
                                    <a <#if user.isAdmin()>href="/tournament/${tournament.getId()}/playOff/${round.getId()}/createPlayOffMatch/tour/2"</#if> class="matchUp matchUp-tourTwo matchUp-unplayed">
                                        <p class="matchUp_team"></p>
                                        <#if user.isAdmin()><i class="fa-solid fa-circle-plus match-add"></i></#if>
                                        <p class="matchUp_team"></p>
                                    </a>
                                </#list>
                                <#break>
                            <#case "Финал">
                                <#list finalMatches as finalMatch>
                                    <a <#if user.isAdmin()>href="/tournament/${tournament.getId()}/playOff/${round.getId()}/createPlayOffMatch/tour/1"</#if> class="matchUp matchUp-unplayed">
                                        <p class="matchUp_team"></p>
                                        <#if user.isAdmin()><i class="fa-solid fa-circle-plus match-add"></i></#if>
                                        <p class="matchUp_team"></p>
                                    </a>
                                </#list>
                                <#break>
                            <#default>
                                <#break>
                        </#switch>
                    </div>
                </#list>
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

    let tourButtons = document.getElementsByClassName("tour");
    tourButtons[0].style.background = "#282c41";
    tourButtons[0].style.color = "white";
    tourButtons[2].style.background = "#282c41";
    tourButtons[2].style.color = "white";
    tourButtons[4].style.background = "#282c41";
    tourButtons[4].style.color = "white";


    for (let i = 0; i < document.getElementsByClassName("matchUp-tourOne").length; i++) {
        document.getElementsByClassName("matchUp-tourOne")[i].classList.add("visible");
    }

    for (let i = 0; i < tourButtons.length; i++) {
        tourButtons[i].onclick = function () {
            if (tourButtons[i].id.substring(0, 5) === "tour1") {
                document.getElementById(tourButtons[i].id).style.background = "#282c41";
                document.getElementById(tourButtons[i + 1].id).style.background = "white";
                document.getElementById(tourButtons[i].id).style.color = "white";
                document.getElementById(tourButtons[i + 1].id).style.color = "#282c41";
                if (i < 2) {
                    for (let j = 0; j < 8; j++) {
                        document.getElementsByClassName("matchUp-tourTwo")[j].classList.remove("visible");
                        document.getElementsByClassName("matchUp-tourOne")[j].classList.add("visible");
                    }
                } else if (i < 4) {
                    for (let j = 8; j < 12; j++) {
                        document.getElementsByClassName("matchUp-tourTwo")[j].classList.remove("visible");
                        document.getElementsByClassName("matchUp-tourOne")[j].classList.add("visible");
                    }
                } else {
                    for (let j = 12; j < 14; j++) {
                        document.getElementsByClassName("matchUp-tourTwo")[j].classList.remove("visible");
                        document.getElementsByClassName("matchUp-tourOne")[j].classList.add("visible");
                    }
                }
            } else {
                document.getElementById(tourButtons[i - 1].id).style.background = "white";
                document.getElementById(tourButtons[i].id).style.background = "#282c41";
                document.getElementById(tourButtons[i - 1].id).style.color = "#282c41";
                document.getElementById(tourButtons[i].id).style.color = "white";
                if (i < 2) {
                    for (let j = 0; j < 8; j++) {
                        document.getElementsByClassName("matchUp-tourTwo")[j].classList.add("visible");
                        document.getElementsByClassName("matchUp-tourOne")[j].classList.remove("visible");
                    }
                } else if (i < 4) {
                    for (let j = 8; j < 12; j++) {
                        document.getElementsByClassName("matchUp-tourTwo")[j].classList.add("visible");
                        document.getElementsByClassName("matchUp-tourOne")[j].classList.remove("visible");
                    }
                } else {
                    for (let j = 12; j < 14; j++) {
                        document.getElementsByClassName("matchUp-tourTwo")[j].classList.add("visible");
                        document.getElementsByClassName("matchUp-tourOne")[j].classList.remove("visible");
                    }
                }
            }
        };
    }

    for (let i = 0; i < document.getElementsByClassName("close").length; i++) {
        document.getElementsByClassName("close")[i].onclick = function () {
            document.getElementsByClassName("match")[i].classList.remove("target");
            document.getElementsByClassName("header")[0].style.zIndex = "10000";
        }
    }

    for (let i = 0; i < document.getElementsByClassName("show").length; i++) {
        document.getElementsByClassName("show")[i].onclick = function () {
            document.getElementsByClassName("match")[i].classList.add("target");
            document.getElementsByClassName("header")[0].style.zIndex = "1000";
        }
    }
</script>
</body>
</html>