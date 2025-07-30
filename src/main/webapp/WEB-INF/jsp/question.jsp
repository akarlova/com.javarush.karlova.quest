<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вопрос ${questionNumber} из ${totalQuestions}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Orbitron:wght@500;700&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>"/>
</head>
<body class="team-bg">

<%-- ТАЙМЕР + КРИСТАЛЛЫ ЖИЗНИ --%>
<c:if test="${not empty lives}">
    <c:choose>
        <c:when test="${sessionScope.teamId == 1}">
            <c:set var="lifeImg" value="/resources/images/stones/Blue_Diamond.png"/>
        </c:when>
        <c:when test="${sessionScope.teamId == 2}">
            <c:set var="lifeImg" value="/resources/images/stones/Yellow_Diamond.png"/>
        </c:when>
        <c:when test="${sessionScope.teamId == 3}">
            <c:set var="lifeImg" value="/resources/images/stones/Red_Diamond.png"/>
        </c:when>
        <c:when test="${sessionScope.teamId == 4}">
            <c:set var="lifeImg" value="/resources/images/stones/Green_Diamond.png"/>
        </c:when>
        <c:otherwise>
            <c:set var="lifeImg" value="/resources/images/stones/Red_Diamond.png"/>
        </c:otherwise>
    </c:choose>
</c:if>

<%-- Единый блок таймера + кристаллов --%>
<div class="timer-and-lives">
    <div id="timer" class="station-clock">
        <span class="time-text">00:00:00</span>
    </div>
    <div class="lives-inline"><c:if test="${not empty lives}">
        <c:forEach var="i" begin="1" end="${lives}">
            <img src="<c:url value='${lifeImg}'/>" alt="life" class="life-icon"/>
        </c:forEach>
    </c:if>
    </div>
</div>


<div class="quiz-question-container container py-5">
    <h4 class="text-center mb-4">
        Вопрос <strong>${questionNumber}</strong> из <strong>${totalQuestions}</strong>
    </h4>

    <%--Текст вопроса--%>

    <div class="lead quiz-question-text text-start">
        <!-- выводим HTML без экранирования -->
        <c:out value="${question.questionText}" escapeXml="false"/>
    </div>


    <c:if test="${not empty error}">
        <div class="alert alert-warning">${error}</div>
    </c:if>
    <form name="quizForm"
          id="quizForm"
          method="post"
          action="${pageContext.request.contextPath}/quiz">

        <input type="hidden" name="questionIndex" value="${questionIndex}"/>
        <input type="hidden" name="testId" value="${testId}"/>

        <%--Варианты ответа--%>

        <div class="list-group">
            <c:forEach var="option" items="${question.options}" varStatus="loop">
                <c:if test="${not empty option}">
                    <label class="list-group-item list-group-item-action">
                        <c:choose>
                            <%--вариант ответа- чекбокс--%>
                            <c:when test="${fn:length(question.correctAnswers) > 1}">
                                <input
                                        type="checkbox"
                                        name="option"
                                        value="${loop.index}"
                                        class="form-check-input me-2"

                                    <%--устранение несохранения ответов, при использовании кнопки Назад--%>
                                        <c:if test="${answers[questionIndex] != null
                                            && answers[questionIndex].contains(loop.index)}">
                                            checked
                                        </c:if>
                                />
                            </c:when>
                            <%--вариант ответа- radio--%>
                            <c:otherwise>
                                <input
                                        type="radio"
                                        name="option"
                                        value="${loop.index}"
                                        required class="form-check-input me-2"
                                        <c:if test="${answers[questionIndex] != null
                                            && answers[questionIndex].contains(loop.index)}">
                                            checked
                                        </c:if>/>
                            </c:otherwise>
                        </c:choose>
                        <span>${option}</span>
                    </label>
                </c:if>
            </c:forEach>
        </div>
        <%--КНОПКИ--%>
        <div class="mt-3 d-flex align-items-center">
            <div>
                <c:if test="${questionNumber > 1}">
                    <button
                            type="submit"
                            name="action"
                            value="goBack"
                            class="btn btn-secondary me-2"
                            formnovalidate>
                        Назад
                    </button>
                </c:if>
                <c:if test="${questionNumber lt totalQuestions}">
                    <button
                            type="submit"
                            name="action"
                            value="submitAnswer"
                            class="btn btn-primary me-2">
                        Далее
                    </button>
                </c:if>
            </div>
            <button
                    type="submit"
                    name="action"
                    value="finishQuiz"
                    class="btn btn-success ms-auto">
                Завершить тест
            </button>
        </div>
    </form>
    <aside class="question-nav">
        <c:forEach begin="0" end="${totalQuestions - 1}" var="i">
            <form method="post" action="${pageContext.request.contextPath}/quiz" class="nav-item-form">
                <input type="hidden" name="action" value="goToQuestion"/>
                <input type="hidden" name="questionIndex" value="${i}"/>
                <button type="submit"
                        class="nav-item btn btn-light
                     ${answers.containsKey(i) ? 'answered' : ''}
                     ${questionIndex == i ? 'current' : ''}"
                        title="Вопрос ${i+1}">
                        ${i+1}
                </button>
            </form>
        </c:forEach>
    </aside>
</div>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>const TOTAL_MS = ${totalMs};
</script>
<script src="<c:url value='/resources/js/timer.js'/>"></script>
</body>
</html>
