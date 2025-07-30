package com.javarush.karlova.quest.service;

import com.javarush.karlova.quest.dao.ResultDao;
import com.javarush.karlova.quest.model.*;
import com.javarush.karlova.quest.util.TestLoader;

import java.util.List;
import java.util.Map;

public class QuizService {

    private final ResultDao resultDao = new ResultDao();

    public QuizState startQuiz(User user, TestEntity stub) {
        int initialLives = user.getRole() == Role.MENTOR ? 2 : 3;
        TestEntity test = TestLoader.getTest(stub.getId());
        QuizState state = new QuizState(test.getId(), initialLives, test.getQuestions());
        return state;
    }

    public void submitAnswer(QuizState state, List<Integer> selectedOption) {
        int index = state.getCurrentQuestionIndex();
        state.getAnswers().put(index, selectedOption);
        state.setCurrentQuestionIndex(index + 1);
    }

    public void submitAnswer(QuizState state, int selectedOption) {
        int index = state.getCurrentQuestionIndex();
        state.getAnswers().put(index, List.of(selectedOption));
        state.setCurrentQuestionIndex(index + 1);
    }

    public boolean canRestartQuiz(QuizState state) {
        return state.getRemainingLives() > 0;
    }

    public void restartQuiz(QuizState state) {
        if (!canRestartQuiz(state)) {
            throw new IllegalStateException("No lives left");
        }
        state.decrementLife();
        state.setCurrentQuestionIndex(0);
        state.getAnswers().clear();

        state.resetStartTime();
    }

    public Result finishQuiz(User user, QuizState state){
        //check the time-out
        long elapsedTime = System.currentTimeMillis() - state.getStartTimeMillis();
        if (elapsedTime > 3_600_000L) {
            throw new IllegalStateException("Time's up");
        }
        //counting the score
        int score = 0;
        List<Question> questions = state.getQuestions();
        Map<Integer, List<Integer>> answers = state.getAnswers();

        for (int i = 0; i < questions.size(); i++) {
            List<Integer> userAnswers = answers.get(i);
            List<Integer> correctAnswers = questions.get(i).getCorrectAnswers();
            System.out.printf(
                    "DEBUG Q%d: user=%s correct=%s%n",
                    i+1, userAnswers, correctAnswers
            );
            if (userAnswers == null) {
                continue;
            }
             if(userAnswers.size()==correctAnswers.size() &&userAnswers.containsAll(correctAnswers)) {
                 score++;
             }
        }
        int durationSeconds = (int) (elapsedTime / 1000);

        //save the result
        Result result = new Result(user.getId(), state.getTestId(), score, durationSeconds);
        return resultDao.saveResult(result);
    }
}
