package com.javarush.karlova.quest.model;

public class TeamScore {
    private Team team;
    private int totalScore;

    public TeamScore() {
    }
    public TeamScore(Team team, int totalScore) {
        this.team = team;
        this.totalScore = totalScore;
    }
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }
    public int getTotalScore() {
        return totalScore;
    }
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
