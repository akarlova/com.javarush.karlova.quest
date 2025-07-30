package com.javarush.karlova.quest.dao;

import com.javarush.karlova.quest.model.Role;
import com.javarush.karlova.quest.model.Team;
import com.javarush.karlova.quest.model.User;
import com.javarush.karlova.quest.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {

    public User save(User user)  {

        Optional<User> existingUser = findByNickname(user.getNickname());
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        String sql = """
                INSERT INTO users (nickname, role, team_id) 
                VALUES (?, ?, ?)
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prepSt =
                     conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepSt.setString(1, user.getNickname());
            prepSt.setString(2, user.getRole().name());
            prepSt.setInt(3, user.getTeam().getId());
            prepSt.executeUpdate();
            try (ResultSet resultSet = prepSt.getGeneratedKeys()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getLong(1));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Не удалось записать пользователя", e);
        }
        return user;
    }

    public Optional<User> findByNickname(String nickname){
        String sql = """
                SELECT id, nickname, role, team_id
                FROM users
                WHERE nickname = ?
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prepSt = conn.prepareStatement(sql)) {
            prepSt.setString(1, nickname);
            try (ResultSet resultSet = prepSt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(userParser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Пользователь с таким никнеймом не найден", e);
        }
        return Optional.empty();
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, nickname, role, team_id FROM users";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prepSt = conn.prepareStatement(sql);
             ResultSet resultSet = prepSt.executeQuery()) {
            while (resultSet.next()) {
                users.add(userParser(resultSet));
            }
        }
        return users;
    }

    private User userParser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setNickname(resultSet.getString("nickname"));
        user.setRole(Role.valueOf(resultSet.getString("role")));
        Team team = new Team();
        team.setId(resultSet.getInt("team_id"));
        user.setTeam(team);
        return user;
    }
}
