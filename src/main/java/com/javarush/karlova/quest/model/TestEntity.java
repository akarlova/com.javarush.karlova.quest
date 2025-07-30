package com.javarush.karlova.quest.model;

import java.util.List;
import java.util.Objects;

/**
 * У теста есть название, уровень сложности и язык
 */
public class TestEntity {
    private Long id;
    private String title;
    private Level level;
    private String language;
    List<Question> questions;

    public TestEntity() {}
    public TestEntity(Long id, String title, Level level, String language) {
        this.id = id;
        this.title = title;
        this.level = level;
        this.language = language;
    }
    public TestEntity(Long id, String title, Level level, String language, List<Question> questions) {
        this(id, title, level, language);
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    public List<Question> getQuestions() {
        return questions;
    }
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestEntity test = (TestEntity) o;
        return Objects.equals(id, test.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TestEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", level=" + level +
                ", language='" + language + '\'' +
                ", questions=" + questions +
                '}';
    }
}
