package com.javarush.karlova.quest.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Question implements Serializable {
    private String questionText;
    private List<String> options;
    private List <Integer> correctAnswers;

    public Question() {}
    public Question(String questionText, List<String> options, List <Integer> correctAnswers) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswers = correctAnswers;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<Integer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(questionText, question.questionText) && Objects.equals(options, question.options) && Objects.equals(correctAnswers, question.correctAnswers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionText, options, correctAnswers);
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionText='" + questionText + '\'' +
                ", options=" + options +
                ", correctAnswers=" + correctAnswers +
                '}';
    }
}
