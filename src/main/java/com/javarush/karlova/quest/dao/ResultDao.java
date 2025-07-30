package com.javarush.karlova.quest.dao;

import com.javarush.karlova.quest.model.Result;
import com.javarush.karlova.quest.model.Role;
import com.javarush.karlova.quest.model.Team;
import com.javarush.karlova.quest.model.TeamScore;
import com.javarush.karlova.quest.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultDao {
    public Result saveResult(Result result) {
        String sql = """
                INSERT INTO results (user_id, test_id, score, duration_seconds)
                VALUES (?,?,?,?)
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, result.getUserId());
            ps.setLong(2, result.getTestId());
            ps.setInt(3, result.getScore());
            ps.setInt(4, result.getDurationSeconds());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    result.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось сохранить результат", e);
        }
        return result;
    }

    public List<Result> findByTestIdSorted(Long testId) {
        String sql = """
                SELECT 
                r.id AS id,
                r.user_id AS user_id,
                r.test_id AS test_id,
                r.score AS score,
                r.duration_seconds AS duration_seconds,
                u.nickname AS user_nickname,
                u.role AS user_role,
                t.avatar_path AS team_avatar_path
                FROM results r
                JOIN users u ON r.user_id = u.id
                JOIN teams t ON u.team_id = t.id
                WHERE r.test_id = ?
                ORDER BY r.score DESC, r.duration_seconds ASC
                """;
        List<Result> leaderList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, testId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Result result = new Result();
                    result.setId(rs.getLong("id"));
                    result.setUserId(rs.getLong("user_id"));
                    result.setTestId(rs.getLong("test_id"));
                    result.setScore(rs.getInt("score"));
                    result.setDurationSeconds(rs.getInt("duration_seconds"));
                    result.setUserName(rs.getString("user_nickname"));
                    result.setRole(Role.valueOf(rs.getString("user_role")));
                    result.setTeamAvatarPath(rs.getString("team_avatar_path"));
                    leaderList.add(result);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при построении рейтинга по тесту", e);
        }
        return leaderList;
    }

    public List<TeamScore> findTeamRanking() {
        String sql = """
                SELECT 
                t.id AS team_id,
                t.name AS team_name,
                t.avatar_path AS team_avatar_path,
                SUM(r.score) AS total_score
                FROM results r
                JOIN users u ON r.user_id = u.id
                JOIN teams t ON u.team_id = t.id
                GROUP BY t.id,t.name,t.avatar_path
                ORDER BY total_score DESC
                """;
        List<TeamScore> leaderTeamList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Team team = new Team();
                team.setId(rs.getInt("team_id"));
                team.setName(rs.getString("team_name"));
                team.setAvatarPath(rs.getString("team_avatar_path"));

                int total = rs.getInt("total_score");
                TeamScore teamScore = new TeamScore(team, total);
                leaderTeamList.add(teamScore);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка формирования командного рейтинга", e);
        }
        return leaderTeamList;
    }
}
