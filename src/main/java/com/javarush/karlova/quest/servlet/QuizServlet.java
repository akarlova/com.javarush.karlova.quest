package com.javarush.karlova.quest.servlet;

import com.javarush.karlova.quest.dao.ResultDao;
import com.javarush.karlova.quest.dao.TeamDao;
import com.javarush.karlova.quest.dao.UserDao;
import com.javarush.karlova.quest.model.*;
import com.javarush.karlova.quest.service.QuizService;
import com.javarush.karlova.quest.util.TestLoader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {
    private final QuizService quizService = new QuizService();
    private final UserDao userDao = new UserDao();
    private final TeamDao teamDao = new TeamDao();
    private final ResultDao resultDao = new ResultDao();

    private static final long TIMEOUT_THRESHOLD_MS = 3_600_000L;
    private static final int TIMEOUT_THRESHOLD_SEC = (int) TIMEOUT_THRESHOLD_MS / 1000;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        //0. Welcome: nickname enter
        if (session.getAttribute("nickname") == null) {
            req.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp")
                    .forward(req, resp);
            return;
        }

        //1.Little pre-quest before quiz through steps
        String step = req.getParameter("step");
        if (step == null) {
            step = "team";
        }
        switch (step) {
            case "team":
            case "role":
            case "difficulty":
            case "list":
                req.setAttribute("lives", session.getAttribute("lives"));
                if ("team".equals(step)) {
                    req.setAttribute("teams", teamDao.findAll());
                }
                if ("list".equals(step)) {
                    String difficulty = (String) session.getAttribute("difficulty");
                    List<TestEntity> tests = TestLoader.getAllTests().values()
                            .stream().filter(t -> t.getLevel().name().equals(difficulty))
                            .toList();
                    req.setAttribute("tests", tests);
                }
                req.setAttribute("step", step);
                req.getRequestDispatcher("/WEB-INF/jsp/preQuest.jsp")
                        .forward(req, resp);
                break;
            case "question":
                QuizState quizState = (QuizState) session.getAttribute("quizState");
                // ТАЙМЕР
                if (isTimeUp(session)) {
                    req.setAttribute("error", "Время вышло");
                    req.setAttribute("durationSeconds", 3600);
                    req.getRequestDispatcher("/WEB-INF/jsp/result.jsp").forward(req, resp);
                    return;
                }
                long elapsed = System.currentTimeMillis() - quizState.getStartTimeMillis();
                long remainingTime = Math.max(0, TIMEOUT_THRESHOLD_MS - elapsed);
                req.setAttribute("totalMs", remainingTime);

                int idx = quizState.getCurrentQuestionIndex();
                Question q = quizState.getQuestions().get(idx);

                req.setAttribute("question", q);
                req.setAttribute("questionNumber", idx + 1);
                req.setAttribute("totalQuestions", quizState.getQuestions().size());
                req.setAttribute("questionIndex", idx);
                req.setAttribute("testId", quizState.getTestId());
                req.setAttribute("answers", quizState.getAnswers());

                req.setAttribute("lives", session.getAttribute("lives"));

                req.getRequestDispatcher("/WEB-INF/jsp/question.jsp")
                        .forward(req, resp);
                break;

            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");
       HttpSession session = req.getSession();

        switch (action) {

            //0.Entering of nickname, welcome.jsp
            case "enterNick" -> {
                String nick = req.getParameter("nickname").trim();
                // *** Проверяем длину ***
                if (nick == null || nick.isEmpty()) {
                    req.setAttribute("error", "Никнейм не может быть пустым");
                    req.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp")
                            .forward(req, resp);
                    return;
                }
                if (nick.length() > 15) {
                    req.setAttribute("error", "Никнейм не должен превышать 15 символов");
                    req.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp")
                            .forward(req, resp);
                    return;
                }


                if (userDao.findByNickname(nick).isPresent()) {
                    req.setAttribute("error", "Этот никнейм уже занят");
                    req.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp")
                            .forward(req, resp);
                } else {
                    session.setAttribute("nickname", nick);
                    resp.sendRedirect(req.getContextPath() + "/quiz");
                }
            }
            //1.Choose a team, step = team
            case "selectTeam" -> {
                int teamId = Integer.parseInt(req.getParameter("teamId"));
                session.setAttribute("teamId", teamId);
                resp.sendRedirect(req.getContextPath() + "/quiz?step=role");
            }
            //2.Choose the Role, step = role
            case "selectRole" -> {
                Role role = Role.valueOf(req.getParameter("role"));
                session.setAttribute("role", role);
                // create user
                String nick = (String) session.getAttribute("nickname");
                Team team = teamDao.findById((Integer) session.getAttribute("teamId"))
                        .orElseThrow();
                User user = userDao.findByNickname(nick)
                        .orElseGet(() -> userDao.save(new User(null, nick, role, team)));
                user.setRole(role);
                userDao.save(user);
                session.setAttribute("user", user);
                int initialLives = (role == Role.MENTOR ? 2 : 3);
                session.setAttribute("lives", initialLives);
                session.setAttribute("testRunCounts", new HashMap<Long, Integer>());
                resp.sendRedirect(req.getContextPath() + "/quiz?step=difficulty");
            }
            //3.Choose the level of quiz,step = difficulty
            case "selectDifficulty" -> {
                String difficulty = req.getParameter("difficulty");
                session.setAttribute("difficulty", difficulty);
                resp.sendRedirect(req.getContextPath() + "/quiz?step=list");
            }
            //4.Start of the test, step = list
            case "startTest" -> {

                Integer lives = (Integer) session.getAttribute("lives");

                if (lives == null) {
                    Role role = (Role) session.getAttribute("role");
                    lives = (role == Role.MENTOR ? 2 : 3);
                    session.setAttribute("lives", lives);
                }
                if (lives <= 0) {
                    req.setAttribute("error", "Жизни закончились. GAME OVER");
                    req.setAttribute("step", "list");
                    String difficulty = (String) session.getAttribute("difficulty");
                    List<TestEntity> tests = TestLoader.getAllTests().values().stream()
                            .filter(t -> t.getLevel().name().equals(difficulty))
                            .toList();
                    req.setAttribute("tests", tests);
                    req.getRequestDispatcher("/WEB-INF/jsp/preQuest.jsp")
                            .forward(req, resp);
                    return;
                }

                //Проверка запуска теста: в какой раз его пытается пройти юзер
                long testId = Long.parseLong(req.getParameter("testId"));
                handleLivesInQuiz(session, lives, testId);

                User user = (User) session.getAttribute("user");
                QuizState newState = quizService.startQuiz(user, new TestEntity(testId,
                        null, null, null));
                session.setAttribute("quizState", newState);

                resp.sendRedirect(req.getContextPath() + "/quiz?step=question");
            }
            //5.Submit answer, question.jsp
            case "submitAnswer" -> {

                if (forwardIfTimeUp(req, resp, session)) {
                    return;
                }
                QuizState state = (QuizState) session.getAttribute("quizState");

                String[] params = req.getParameterValues("option");
                if (params == null || params.length == 0) {
                    int idx = state.getCurrentQuestionIndex();
                    Question q = state.getQuestions().get(idx);
                    long elapsed = System.currentTimeMillis() - state.getStartTimeMillis();
                    long remainingMs = Math.max(0, TIMEOUT_THRESHOLD_MS - elapsed);
                    req.setAttribute("totalMs", remainingMs);

                    req.setAttribute("error", "Пожалуйста, выбери хотя бы один вариант");
                    req.setAttribute("question", q);
                    req.setAttribute("questionNumber", idx + 1);
                    req.setAttribute("totalQuestions", state.getQuestions().size());
                    req.setAttribute("questionIndex", idx);
                    req.setAttribute("testId", state.getTestId());
                    req.setAttribute("answers", state.getAnswers());
                    req.setAttribute("lives", session.getAttribute("lives"));
                    req.getRequestDispatcher("/WEB-INF/jsp/question.jsp")
                            .forward(req, resp);
                    return;
                }

                List<Integer> selectedOptions = Arrays.stream(params)
                        .map(Integer::parseInt)
                        .toList();
                quizService.submitAnswer(state, selectedOptions);

                resp.sendRedirect(req.getContextPath() + "/quiz?step=question");
            }

            //6. Re-answer, question.jsp
            case "goBack" -> {

                if (forwardIfTimeUp(req, resp, session)) {
                    return;
                }
                QuizState state = (QuizState) session.getAttribute("quizState");

                //сохранение ответа на текущий вопрос
                String[] params = req.getParameterValues("option");
                if (params != null) {
                    List<Integer> selected = Arrays.stream(params)
                            .map(Integer::parseInt)
                            .toList();
                    state.getAnswers().put(state.getCurrentQuestionIndex(), selected);
                }
                //листаем назад, чтобы поменять ответ на какой-нибудь вопрос
                int idx = state.getCurrentQuestionIndex();
                if (idx > 0) {
                    state.setCurrentQuestionIndex(idx - 1);
                }
                resp.sendRedirect(req.getContextPath() + "/quiz?step=question");
            }

            //7. Finish quiz, result.jsp
            case "finishQuiz" -> {
                if (forwardIfTimeUp(req, resp, session)) {
                    return;
                }
                QuizState state = (QuizState) session.getAttribute("quizState");
                String[] params = req.getParameterValues("option");
                if (params != null) {
                    List<Integer> selectedOptions = Arrays.stream(params)
                            .map(Integer::parseInt)
                            .toList();
                    quizService.submitAnswer(state, selectedOptions);
                }
                User user = (User) session.getAttribute("user");
                Result result = quizService.finishQuiz(user, state);
                req.setAttribute("result", result);
                //РЕЙТИНГ

                //рейтинг по текущему тесту
                List<Result> leaderBoard = resultDao.findByTestIdSorted(result.getTestId());
                int rank = 0;
                for (int i = 0; i < leaderBoard.size(); i++) {
                    if (leaderBoard.get(i).getUserId().equals(result.getUserId())) {
                        rank = i + 1;
                        break;
                    }
                }
                req.setAttribute("leaderBoard", leaderBoard);
                req.setAttribute("userRank", rank);

                //рейтинг команд по всем тестам
                List<TeamScore> teamLeaderBoard = resultDao.findTeamRanking();
                req.setAttribute("teamLeaderBoard", teamLeaderBoard);

                req.getRequestDispatcher("/WEB-INF/jsp/result.jsp")
                        .forward(req, resp);
            }

            //8.Restart quiz
            case "restartQuiz" -> {
                Integer remainingLives = (Integer) session.getAttribute("lives");
                if (remainingLives == null || remainingLives <= 0) {
                    req.setAttribute("error", "Жизней больше нет");
                    req.getRequestDispatcher("/WEB-INF/jsp/result.jsp")
                            .forward(req, resp);
                    return;
                }

                QuizState oldState = (QuizState) session.getAttribute("quizState");
                long testId = oldState.getTestId();
                handleLivesInQuiz(session, remainingLives, testId);
                quizService.restartQuiz(oldState);

                resp.sendRedirect(req.getContextPath() + "/quiz?step=question");
            }
            // 9. go to question for aside table logic
            case "goToQuestion" -> {
                QuizState state = (QuizState) session.getAttribute("quizState");
                int idx = Integer.parseInt(req.getParameter("questionIndex"));
                state.setCurrentQuestionIndex(idx);
                resp.sendRedirect(req.getContextPath() + "/quiz?step=question");
            }
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    private boolean isTimeUp(HttpSession session) {
        QuizState quizState = (QuizState) session.getAttribute("quizState");
        if (quizState == null) {
            return false;
        }
        long elapsed = System.currentTimeMillis() - quizState.getStartTimeMillis();
        return elapsed > TIMEOUT_THRESHOLD_MS;
    }

    private boolean forwardIfTimeUp(HttpServletRequest req, HttpServletResponse resp,
                                    HttpSession session) throws ServletException, IOException {
        if (isTimeUp(session)) {
            req.setAttribute("error", "Время вышло");
            req.setAttribute("durationSeconds", TIMEOUT_THRESHOLD_SEC);

            QuizState quizState = (QuizState) session.getAttribute("quizState");
            long testId = quizState.getTestId();

            List<Result> leaderBoard = resultDao.findByTestIdSorted(testId);
            req.setAttribute("leaderBoard", leaderBoard);

            List<TeamScore> teamLeaderBoard = resultDao.findTeamRanking();
            req.setAttribute("teamLeaderBoard", teamLeaderBoard);

            req.getRequestDispatcher("/WEB-INF/jsp/result.jsp")
                    .forward(req, resp);
            return true;
        }
        return false;
    }

    private void handleLivesInQuiz(HttpSession session, Integer lives, long testId) {
        @SuppressWarnings("unchecked")
        Map<Long, Integer> runCounts = (Map<Long, Integer>) session.getAttribute("testRunCounts");
        if (runCounts == null) {
            runCounts = new HashMap<>();
            session.setAttribute("testRunCounts", runCounts);
        }
        int prev = runCounts.getOrDefault(testId, 0);
        if (prev > 0) {
            session.setAttribute("lives", lives - 1);
        }
        runCounts.put(testId, prev + 1);
    }
}
