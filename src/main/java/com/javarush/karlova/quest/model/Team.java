package com.javarush.karlova.quest.model;

import java.io.Serializable;
import java.util.Objects;

public class Team implements Serializable {
    private  Integer id;
    private String name;
    private String avatarPath;

    public Team() {}

    public Team(Integer id, String name, String avatarPath) {
        this.id = id;
        this.name = name;
        this.avatarPath = avatarPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
