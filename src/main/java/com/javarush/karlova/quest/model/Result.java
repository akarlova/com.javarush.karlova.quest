package com.javarush.karlova.quest.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Result implements Serializable {
    private Long id;
    private Long userId;
    private Long testId;
    private int score;
    private int durationSeconds;
    private LocalDateTime takenAt;

    private String userName;
    private String teamAvatarPath;
    private Role role;

    public Result() {
    }

    public Result(Long userId, Long testId, int score, int durationSeconds) {
        this.userId = userId;
        this.testId = testId;
        this.score = score;
        this.durationSeconds = durationSeconds;
        this.takenAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(LocalDateTime takenAt) {
        this.takenAt = takenAt;
    }

    public String getTeamAvatarPath() {
        return teamAvatarPath;
    }

    public void setTeamAvatarPath(String teamAvatarPath) {
        this.teamAvatarPath = teamAvatarPath;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public String getFormattedDuration() {
        int rawDuration = this.durationSeconds;
        int h = rawDuration / 3600;
        int min = (rawDuration % 3600) / 60;
        int sec = rawDuration % 60;
        return String.format("%02d:%02d:%02d", h, min, sec);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(id, result.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
