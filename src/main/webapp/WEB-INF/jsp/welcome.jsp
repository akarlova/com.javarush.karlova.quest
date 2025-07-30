<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>

    <!--Bootstrap CSS -->
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet"/>

    <!--Мои стили-->
    <link rel="stylesheet" href="<c:url value = '/resources/css/style.css'/>"/>

    <!--Google fonts-->
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link
            href="https://fonts.googleapis.com/css2?family=Cinzel:wght@700&display=swap"
            rel="stylesheet"/>

    <script>
        function onNickInput(el) {
            document.getElementById('btnStart').disabled = el.value.trim() === '';
        }
    </script>
</head>

<body class="bg-video-body">
<%--Видеофон--%>
<video id="bg-video" autoplay muted loop>
    <source src="<c:url value='/resources/videos/torches_loop.mp4'/>" type="video/mp4">
    Твой браузер не поддерживает видео.
</video>
<%--Заголовок--%>
<div class="greeting">
    <h1 class="greeting-title">Идущие на оффер </h1>
    <h1 class="greeting-title">приветствуют тебя! </h1>
</div>

<div class="scroll-container">
    <form method="post"
          action="<c:url value='/quiz'/>">
        <input type="hidden" name="action" value="enterNick"/>


                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">${error}</div>
                </c:if>

                    <label for="nickname" >Nickname/Никнейм</label>
                    <input type="text"
                           id="nickname"
                           name="nickname"
                           maxlength="15"
                           oninput="onNickInput(this)"
                           required
                           autofocus/>

                <button type="submit"
                        id="btnStart"
                        disabled>
                    Начать турнир!
                </button>
    </form>
</div>

<%--Кубочек--%>
<div>
    <img
            src="<c:url value='/resources/images/cool_elements/java_cup.png'/>"
            alt="Java Cup"
            class="java-cup-img"
    />
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js">

</script>

</body>
</html>
