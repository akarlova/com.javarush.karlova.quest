<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="step" class="java.lang.String" scope="request"/>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Prequel</title>
    <!--Bootstrap CSS -->
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet"/>
    <!--Мои стили-->
    <link rel="stylesheet" href="<c:url value = '/resources/css/style.css'/>"/>
    <!--Google fonts-->
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link href="https://fonts.googleapis.com/css2?family=Uncial+Antiqua&display=swap" rel="stylesheet"/>
</head>
<body class="team-bg" style="font-family: 'Uncial Antiqua', cursive">

<%--ОСНОВНОЙ БЛОК--%>
<c:choose>
    <%--1)Выбор команды--%>
    <c:when test="${step=='team'}">
        <%--Поле с вопросом--%>
        <%--Камни слева--%>
        <div class="question-container">
            <img src="<c:url value='/resources/images/stones/Red_Diamond.png'/>"
                 alt=""
                 class="stone stone-left first">
            <img src="<c:url value='/resources/images/stones/Yellow_Diamond.png'/>"
                 alt=""
                 class="stone stone-left second">
            <img src="<c:url value='/resources/images/stones/Green_Diamond.png'/>"
                 alt=""
                 class="stone stone-left third">
            <img src="<c:url value='/resources/images/stones/Blue_Diamond.png'/>"
                 alt=""
                 class="stone stone-left fourth">
                <%--Камни справа--%>
            <img src="<c:url value='/resources/images/stones/Green_Diamond.png'/>"
                 alt=""
                 class="stone stone-right first">
            <img src="<c:url value='/resources/images/stones/Blue_Diamond.png'/>"
                 alt=""
                 class="stone stone-right second">
            <img src="<c:url value='/resources/images/stones/Red_Diamond.png'/>"
                 alt=""
                 class="stone stone-right third">
            <img src="<c:url value='/resources/images/stones/Yellow_Diamond.png'/>"
                 alt=""
                 class="stone stone-right fourth">

            <h2 class="question-text">
                К какой команде присоединишься,&nbsp;
                <strong>${sessionScope.nickname}</strong>?
            </h2>
        </div>

        <div class="quiz-container">
            <form method="post" action="${pageContext.request.contextPath}/quiz">
                <input type="hidden" name="action" value="selectTeam"/>
                <div class="teams-grid">
                    <c:forEach var="team" items="${teams}">
                        <label class="team-card">
                            <input type="radio" name="teamId" value="${team.id}" required class="d-none"/>
                            <div class="card-content">
                                <div class="avatar-container">
                                    <img src="<c:url value='/resources/images/avatars/${team.avatarPath}'/>"
                                         alt="${team.name}"
                                         class="avatar-img"/>
                                </div>
                                <div class="team-name">${team.name}</div>
                            </div>

                        </label>

                    </c:forEach>
                </div>
                <div class="submit-container">
                    <button type="submit" class="btn">Подтвердить выбор</button>
                </div>
            </form>
        </div>
    </c:when>

    <%--2)Выбор роли--%>
    <c:when test="${step == 'role'}">
        <div class="question-container">
            <img src="<c:url value='/resources/images/stones/Red_Diamond.png'/>"
                 alt=""
                 class="stone stone-left first">
            <img src="<c:url value='/resources/images/stones/Yellow_Diamond.png'/>"
                 alt=""
                 class="stone stone-left second">
            <img src="<c:url value='/resources/images/stones/Green_Diamond.png'/>"
                 alt=""
                 class="stone stone-left third">
            <img src="<c:url value='/resources/images/stones/Blue_Diamond.png'/>"
                 alt=""
                 class="stone stone-left fourth">
                <%--Камни справа--%>
            <img src="<c:url value='/resources/images/stones/Green_Diamond.png'/>"
                 alt=""
                 class="stone stone-right first">
            <img src="<c:url value='/resources/images/stones/Blue_Diamond.png'/>"
                 alt=""
                 class="stone stone-right second">
            <img src="<c:url value='/resources/images/stones/Red_Diamond.png'/>"
                 alt=""
                 class="stone stone-right third">
            <img src="<c:url value='/resources/images/stones/Yellow_Diamond.png'/>"
                 alt=""
                 class="stone stone-right fourth">

            <h2 class="question-text">${sessionScope.nickname}, ты ментор или студент?</h2>
        </div>
        <div class="quiz-container">
            <form method="post" action="${pageContext.request.contextPath}/quiz">
                <input type="hidden" name="action" value="selectRole"/>

                <div class="teams-grid">
                    <label class="team-card">
                        <input type="radio" name="role" value="STUDENT" required class="d-none"/>
                        <div class="card-content">
                            <!-- пустой блок аватарки, чтобы не сломать отступы -->
                            <div class="avatar-container" style="visibility: hidden;"></div>
                            <div class="team-level">СТУДЕНТ</div>
                        </div>
                    </label>

                    <label class="team-card">
                        <input type="radio" name="role" value="MENTOR" required class="d-none"/>
                        <div class="card-content">
                            <div class="avatar-container" style="visibility: hidden;"></div>
                            <div class="team-level">МЕНТОР</div>
                        </div>
                    </label>
                </div>
                <div class="beast-container">
                    <img src="<c:url value='/resources/images/avatars/Puffin_Ravenclaw_Icon.png'/>"
                         alt="Magic Trophy"
                    class="beast-icon avatar–flip"/>
                    <img src="<c:url value='/resources/images/avatars/Slytherin_Raptor_Icon.png'/>"
                         alt="Magic Trophy"
                    class="beast-icon"/>
                </div>
                <div class="submit-container">
                    <button type="submit" class="btn">Далее</button>
                </div>
            </form>
        </div>
    </c:when>

    <%--3)Выбор сложности--%>
    <c:when test="${step=='difficulty'}">
<div class="question-container">
    <img src="<c:url value='/resources/images/stones/Red_Diamond.png'/>"
         alt=""
         class="stone stone-left first">
    <img src="<c:url value='/resources/images/stones/Yellow_Diamond.png'/>"
         alt=""
         class="stone stone-left second">
    <img src="<c:url value='/resources/images/stones/Green_Diamond.png'/>"
         alt=""
         class="stone stone-left third">
    <img src="<c:url value='/resources/images/stones/Blue_Diamond.png'/>"
         alt=""
         class="stone stone-left fourth">
        <%--Камни справа--%>
    <img src="<c:url value='/resources/images/stones/Green_Diamond.png'/>"
         alt=""
         class="stone stone-right first">
    <img src="<c:url value='/resources/images/stones/Blue_Diamond.png'/>"
         alt=""
         class="stone stone-right second">
    <img src="<c:url value='/resources/images/stones/Red_Diamond.png'/>"
         alt=""
         class="stone stone-right third">
    <img src="<c:url value='/resources/images/stones/Yellow_Diamond.png'/>"
         alt=""
         class="stone stone-right fourth">

    <h2 class="question-text">Какой уровень сложности предпочитаешь?</h2>
</div>
<div class="quiz-container">
        <form method="post" action="${pageContext.request.contextPath}/quiz">
            <input type="hidden" name="action" value="selectDifficulty"/>
            <div class="teams-grid">
                <label class="team-card">
                    <input type="radio" name="difficulty" value="EASY" required class="d-none"/>
                    <div class="card-content">
                        <div class="avatar-container" style="visibility: hidden;"></div>
                        <div class="team-level">Простой</div>
                    </div>
                </label>
                <label class="team-card">
                    <input type="radio" name="difficulty" value="HARD" required class="d-none"/>
                    <div class="card-content">
                        <div class="avatar-container" style="visibility: hidden;"></div>
                        <div class="team-level">Сложный </div>
                    </div>
                </label>
            </div>
            <div class="beast-container">
                <img src="<c:url value='/resources/images/avatars/Gryffindor_Kitty_Icon.png'/>"
                     alt="Magic Trophy"
                     class="beast-icon"/>
                <img src="<c:url value='/resources/images/avatars/Deer_HufflePuff_Icon.png'/>"
                     alt="Magic Trophy"
                     class="beast-icon"/>
            </div>
            <div class="submit-container">
            <button type="submit" class="btn">Далее</button>
            </div>
        </form>
</div>
    </c:when>

    <%--4)Список тестов--%>
    <c:when test="${step=='list'}">
<div class="question-container">

        <img src="<c:url value='/resources/images/stones/Red_Diamond.png'/>"
             alt=""
             class="stone stone-left first">
        <img src="<c:url value='/resources/images/stones/Yellow_Diamond.png'/>"
             alt=""
             class="stone stone-left second">
        <img src="<c:url value='/resources/images/stones/Green_Diamond.png'/>"
             alt=""
             class="stone stone-left third">
        <img src="<c:url value='/resources/images/stones/Blue_Diamond.png'/>"
             alt=""
             class="stone stone-left fourth">
            <%--Камни справа--%>
        <img src="<c:url value='/resources/images/stones/Green_Diamond.png'/>"
             alt=""
             class="stone stone-right first">
        <img src="<c:url value='/resources/images/stones/Blue_Diamond.png'/>"
             alt=""
             class="stone stone-right second">
        <img src="<c:url value='/resources/images/stones/Red_Diamond.png'/>"
             alt=""
             class="stone stone-right third">
        <img src="<c:url value='/resources/images/stones/Yellow_Diamond.png'/>"
             alt=""
             class="stone stone-right fourth">


        <h2 class="question-text">Доступные тесты (${sessionScope.difficulty=='EASY'? 'Простые': 'Сложные'})</h2>
</div>
        <%--Обработка ошибки при отсутствии жизней--%>
        <c:if test="${not empty error}">
            <div class="alert alert-danger my-3">
                    ${error}
            </div>
        </c:if>

<!-- общий контейнер для двух колонок -->
<div class="list-and-rules-grid">
    <!-- ЛЕВАЯ КОЛОНКА: доска со списком и кнопкой -->
    <div class="wood-board-card tests-column">
        <form method="post" action="${pageContext.request.contextPath}/quiz">
            <input type="hidden" name="action" value="startTest"/>
            <ul class="list-group test-list">
                <c:forEach var="t" items="${tests}">
                    <li class="list-group-item">
                        <input type="radio" name="testId" value="${t.id}" required/>
                            ${t.title} (${t.language})
                    </li>
                </c:forEach>
            </ul>
            <div class="submit-container">
            <button type="submit" class="btn start-test-btn">Начать тест!</button>
            </div>
            <div class="team-logo-container">
                <img src="<c:url value='/resources/images/cool_elements/team.png'/>"
                     alt="Team Logo"
                     class="team-logo"/>
            </div>
        </form>
    </div>

    <!-- ПРАВАЯ КОЛОНКА: доска с правилами без collapse -->
        <div class="wood-board-card rules-column">
            <h3 class="rules-title">Ограничения квизов:</h3>
            <p>
                1. У Студента 3 жизни, у Ментора - 2.
                Доступные жизни отображаются в виде кристаллов вверху экрана.
                Цвет кристаллов соответствует выбранной команде.<br/>
                2. На прохождение любого квиза дается 1 час. Квиз может быть завершен досрочно.<br/>
                3. Если время истекло и кнопка "Завершить тест" не была нажата - прогресс не сохраняется.<br/>
                4. Квиз можно перепройти заново, но за каждую повторную попытку снимается 1 жизнь.<br/>
                5. Результаты пользователя сохраняются по введенному никнейму только в течение сессии.<br/>
                6. Таблицы результатов и рейтинга доступны после прохождения квиза, на странице результата. <br/>
                7. Удачи в прохождении!<br/>
            </p>
        </div>
</div>
    </c:when>

    <c:otherwise>
        <p>А что ты пытаешься сделать, мм?</p>
    </c:otherwise>
</c:choose>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js">

</script>
</body>
</html>
