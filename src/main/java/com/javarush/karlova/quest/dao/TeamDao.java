package com.javarush.karlova.quest.dao;

import com.javarush.karlova.quest.model.Team;
import com.javarush.karlova.quest.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamDao {
    public List<Team> findAll()  {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT id, name, avatar_path FROM teams ORDER BY id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prepSt = conn.prepareStatement(sql);
             ResultSet resultSet = prepSt.executeQuery()) {
            while (resultSet.next()) {
                teams.add(teamParser(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось загрузить список команд",e);
        }
        return teams;
    }

    public Optional<Team> findById(int id) {
        String sql = "SELECT id, name, avatar_path FROM teams WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prepSt = conn.prepareStatement(sql)) {
            prepSt.setInt(1, id);
            try (ResultSet resultSet = prepSt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(teamParser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска команды по ID",e);
        }
        return Optional.empty();
    }

    private Team teamParser(ResultSet rs) throws SQLException {
        Team t = new Team();
        t.setId(rs.getInt("id"));
        t.setName(rs.getString("name"));
        t.setAvatarPath(rs.getString("avatar_path"));
        return t;
    }
}
