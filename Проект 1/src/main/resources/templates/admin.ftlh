<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Панель администратора</title>
    <link rel="icon" type="image/x-icon" href="/static/img/uefa.ico">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/css/admin.css">
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
        <div class="administrationPanel">
            <h1>Панель администратора</h1>
            <ul class="users">
                <#list users as otherUser>
                    <#if otherUser != user>
                        <li class="user">
                            <div class="user-header">
                                <div class="info">
                                    <h2>${otherUser.getName()}</h2>
                                    <div class="info-block">
                                        <b>Информация:</b>
                                        <ul>
                                            <li>Телефон: ${otherUser.getPhone()}</li>
                                            <li>Почта: ${otherUser.getEmail()}</li>
                                        </ul>
                                    </div>
                                </div>
                                <ul class="roles">
                                    <h3>Роли:</h3>
                                    <#list otherUser.roles as role>
                                        <li class="role">
                                            ${role.toString()}
                                        </li>
                                    </#list>
                                </ul>
                            </div>
                            <div class="user-footer">
                                <div class="status-container">
                                    <p>Статус:</p>
                                    <h4> <#if otherUser.isActive()>
                                            <span class="status">Активен<i class="fa-solid fa-check"></i></span>
                                        <#else>
                                            <span class="status">Заблокирован<i class="fa-solid fa-xmark"></i></span>
                                        </#if></h4>
                                </div>
                                <div class="buttons">
                                    <#if otherUser.isActive()>
                                        <a class="button set" href="/user/${otherUser.getId()}/ban">Заблокировать</a>
                                    <#else>
                                        <a class="button remove" href="/user/${otherUser.getId()}/unban">Разблокировать</a>
                                    </#if>
                                    <#if otherUser.isOperator()>
                                        <a class="button set" href="/user/${otherUser.getId()}/removeOperator">Убрать оператора</a>
                                    <#else>
                                        <a class="button remove" href="/user/${otherUser.getId()}/setOperator">Назначить оператором</a>
                                    </#if>
                                </div>
                            </div>
                        </li>
                    </#if>
                </#list>
            </ul>
        </div>
    </div>
</main>
</body>
</html>