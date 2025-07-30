package com.javarush.karlova.quest.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizState implements Serializable {
    private final Long testId;
    private int remainingLives;
    private int currentQuestionIndex = 0;
    private Long startTimeMillis;

    private final List<Question> questions;
    private final Map<Integer, List<Integer>> answers = new HashMap<>();



    public QuizState(Long testId, int initialLives, List<Question> questions) {
        this.testId = testId;
        this.startTimeMillis = System.currentTimeMillis();
        this.remainingLives = initialLives;
        this.questions = questions;
    }

    public Long getTestId() {
        return testId;
    }
    public long getStartTimeMillis() {
        return startTimeMillis;
    }
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
    public void setCurrentQuestionIndex(int i) {
        this.currentQuestionIndex = i;
    }
    public Map<Integer, List<Integer>> getAnswers() {
        return answers;
    }
    public List<Question> getQuestions() {
        return questions;
    }
    public int getRemainingLives() {
        return remainingLives;
    }
    public void decrementLife(){
        this.remainingLives--;
    }
    public void resetStartTime() {
        this.startTimeMillis = System.currentTimeMillis();
    }

}
