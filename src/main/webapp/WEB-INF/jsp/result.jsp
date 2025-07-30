<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Результаты теста</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>"/>
</head>

<body>

<div class="section-video">
    <%--Видеофон--%>
    <video id="bg-video" autoplay muted loop>
        <source src="<c:url value='/resources/videos/torches_loop.mp4'/>" type="video/mp4">
        Твой браузер не поддерживает видео.
    </video>
    <div class="video-overlay">
        <div class="result-wrap">
            <%--ТАЙМЕР: итоговое время--%>
            <c:if test="${not empty error}">
                <div class="alert alert-danger my-3">${error}</div>
            </c:if>


            <div class="container py-5 text-center">
                <div class="result-header d-flex align-items-center justify-content-center mb-2">
                    <h2 class="greeting-title mb-0">ТЕСТ<br/> ЗАВЕРШЁН</h2>
                </div>

                <div class="card result-summary mx-auto">
                    <div class="card-body">
                        <p class="card-text">
                            Правильных ответов:
                            <strong>${result.score}</strong>
                        </p>
                        <p class="card-text">
                            Затраченное время: <strong>
                            ${result.formattedDuration}

                        </strong>
                        </p>
                        <c:if test="${userRank > 0}">
                            <p class="card-text">
                                Место в рейтинге: <strong>${userRank}</strong>
                            </p>
                        </c:if>
                    </div>
                </div>

                <div class="result-actions-wrapper text-center">
                    <form method="post" action="${pageContext.request.contextPath}/quiz"
                          class="d-inline-block mb-2">
                        <button
                                type="submit"
                                name="action"
                                value="restartQuiz"
                                class="btn btn-success me-2">
                            Пройти тест ещё раз
                        </button>
                        <a href="${pageContext.request.contextPath}/quiz?step=difficulty"
                           class="btn btn-secondary">
                            Вернуться к выбору тестов
                        </a>
                    </form>
                </div>
                <div class="rating-hint text-center">
                    ПОСМОТРЕТЬ РЕЙТИНГ ⬇
                </div>
            </div>
        </div>
    </div>
</div>
<div class="section-image">
    <%--Рейтинг игроков по текущему тесту--%>
    <details>
        <summary class="h3">Рейтинг по этому тесту</summary>
        <table class="table table-striped align-middle">
            <thead>
            <tr>
                <th class="text-center">Место</th>
                <th class="text-center">Команда</th>
                <th class="text-center">Игрок</th>
                <th class="text-center">Статус</th>
                <th class="text-center">Баллы</th>
                <th class="text-center">Время</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="r" items="${leaderBoard}" varStatus="loop">
                <tr <c:if test="${r.userId == result.userId}"> class="table-success fw-bold"</c:if>>

                    <td class="text-center fw-bold">
                        <c:choose>
                            <c:when test="${loop.index == 0}">
                                <img src="<c:url value='/resources/images/cool_elements/1st_place.png'/>"
                                     alt="1st" class="trophy-icon"/>
                            </c:when>
                            <c:when test="${loop.index == 1}">
                                <img src="<c:url value='/resources/images/cool_elements/2nd_place.png'/>"
                                     alt="2nd" class="trophy-icon"/>
                            </c:when>
                            <c:when test="${loop.index == 2}">
                                <img src="<c:url value='/resources/images/cool_elements/3rd_place.png'/>"
                                     alt="3rd" class="trophy-icon"/>
                            </c:when>
                            <c:otherwise>
                                ${loop.index + 1}
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td class="text-center">
                        <img
                                src="<c:url value='/resources/images/avatars/${r.teamAvatarPath}'/>"
                                alt="Аватар команды"
                                class="team-avatar me-2"
                        />
                    </td>
                    <td class="text-center fw-bold">${r.userName}</td>
                    <td class="text-center fw-bold">${r.role}</td>
                    <td class="text-center fw-bold">${r.score}</td>
                    <td class="text-center fw-bold">${r.formattedDuration}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </details>

    <%--Рейтинг команд (по всем тестам итого)--%>
    <details class="mt5">
        <summary class="h3">Командный рейтинг по всем тестам</summary>
        <table class="table table-striped align-middle">
            <colgroup>
                <col style="width: 10%"/>
                <col style="width: 10%"/>
                <col style="width: 10%"/>
                <col style="width: 40%"/>
                <col style="width: 30%"/>
            </colgroup>
            <thead>
            <tr>
                <th class="text-center">Место</th>
                <th class="text-center"></th>
                <th class="text-center"></th>
                <th class="text-center">Команда</th>
                <th class="text-center">Общий балл</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="ts" items="${teamLeaderBoard}" varStatus="loop">
                <tr>
                    <td class="text-center place-number">${loop.index+1}</td>

                    <td class="text-center">

                        <c:url var="cupBlue"    value="/resources/images/cool_elements/blue_cup.png"/>
                        <c:url var="cupGold"    value="/resources/images/cool_elements/gold_cup.png"/>
                        <c:url var="cupRed"     value="/resources/images/cool_elements/red_cup.png"/>
                        <c:url var="cupGreen"   value="/resources/images/cool_elements/green_cup.png"/>

                        <c:choose>
                            <c:when test="${fn:contains(ts.team.avatarPath,'Puffin')}">
                                <img src="
                        ${cupBlue}" alt="Blue Cup" class="cup-icon text-center"/>
                    </c:when>
                    <c:when test="${fn:contains(ts.team.avatarPath,'HufflePuff')}">
                        <img src="${cupGold}" alt="Gold Cup" class="cup-icon "/>
                    </c:when>
                    <c:when test="${fn:contains(ts.team.avatarPath,'Gryffindor')}">
                        <img src="${cupRed}" alt="Red Cup" class="cup-icon"/>
                    </c:when>
                    <c:otherwise>
                        <img src="${cupGreen}" alt="Green Cup" class="cup-icon"/>
                    </c:otherwise>
                    </c:choose>
                    </td>
                    <td class="text-center">
                        <img
                                src="<c:url value='/resources/images/avatars/${ts.team.avatarPath}'/>"
                                alt="Аватар команды"
                                width="40" height="auto"
                                class="team-avatar"
                        />
                    </td>

                    <td class="team-name text-center">
                        <span>${ts.team.name}</span>

                    </td>
                    <td class="text-center points-number">${ts.totalScore}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </details>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
