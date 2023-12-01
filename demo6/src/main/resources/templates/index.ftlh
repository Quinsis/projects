<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/index.css">
</head>
<body>
    <section class="main">
        <div class="navbar">
            <#if user?has_content>
                <h2>Приветствуем, ${user.getName()}!</h2>
                <a class="navbar-link" href="/logout">Выйти</a>
            <#else>
                <a class="navbar-link" href="/login">Войти</a>
            </#if>
        </div>
        <div class="panel">
            <h1 class="panel_title">Попробуйте нашу среду для изучения SQL</h1>
            <div class="panel_tools">
                <a class="tool" href="/sandbox">
                    <i class="fa-solid fa-arrow-right"></i>
                    <span class="tool-title">Песочница</span>
                    <small class="tool-desc">Интерактивная платформа для создания собственных баз данных и запросов.</small>
                </a>
                <a class="tool" href="/role">
                    <i class="fa-solid fa-arrow-right"></i>
                    <span class="tool-title">Задания</span>
                    <small class="tool-desc">Делитесь, выполняйте и создавайте персональные задания для обучения</small>
                </a>
            </div>
        </div>
    </section>
</body>
</html>