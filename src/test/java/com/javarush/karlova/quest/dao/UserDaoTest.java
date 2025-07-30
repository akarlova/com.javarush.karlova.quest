package com.javarush.karlova.quest.dao;

import com.javarush.karlova.quest.model.Role;
import com.javarush.karlova.quest.model.Team;
import com.javarush.karlova.quest.model.User;
import com.javarush.karlova.quest.util.DBConnection;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class UserDaoTest {
    private static UserDao dao;

    @BeforeAll
    static void init() throws Exception {
        dao = new UserDao();
        try (Connection conn = DBConnection.getConnection();
             Statement state = conn.createStatement()) {
            state.execute("RUNSCRIPT FROM 'classpath:schema.sql'");
            state.executeUpdate("DELETE FROM results");
            state.executeUpdate("DELETE FROM users");
            state.executeUpdate("""
                    MERGE INTO TEAMS(ID,NAME,AVATAR_PATH) KEY(ID)
                    VALUES
                      (1,'PuffinClaw','src/main/webapp/resources/images/avatars/Puffin_Ravenclaw_Icon.png'),
                      (2,'DeerPuff','src/main/webapp/resources/images/avatars/Deer_HufflePuff_Icon.png'),
                      (3,'KittenDor','src/main/webapp/resources/images/avatars/Gryffindor_Kitty_Icon.png'),
                      (4,'RaptorRin','src/main/webapp/resources/images/avatars/Slytherin_Raptor_Icon.png')
                    """);


        }
    }

    @Order(1)
    @DisplayName("Check if saving a new user and then looking for him by the nickname works")
    @ParameterizedTest
    @CsvFileSource(resources = "/users.csv", numLinesToSkip = 1)
    void saveAndFindByNicknameTest(String nickname, String role, int teamId) throws Exception {
        Team team = new Team(teamId, null, null);
        User user = new User(null, nickname, Role.valueOf(role), team);

        User savedUser = dao.save(user);
        assertNotNull(savedUser.getId(), "Saved user should have ID");

        Optional<User> fetched = dao.findByNickname(nickname);
        assertTrue(fetched.isPresent(), "User not found");
        assertEquals(savedUser.getNickname(), fetched.get().getNickname(), "User nickname should match");
        assertEquals(teamId, fetched.get().getTeam().getId(), "User team should match");
    }

    @Order(2)
    @Test
    void findAllTest() throws Exception {
        List<User> allUsers = dao.findAll();
        assertNotNull(allUsers, "All users should not be null");
        assertFalse(allUsers.isEmpty(), "All users should not be empty");
    }
}
