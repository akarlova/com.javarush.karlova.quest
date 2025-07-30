package com.javarush.karlova.quest.dao;

import com.javarush.karlova.quest.model.Team;
import com.javarush.karlova.quest.util.DBConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TeamDaoTest {
    private static TeamDao dao;

    @BeforeAll
    static void init() throws Exception {
        dao = new TeamDao();
        try (Connection conn = DBConnection.getConnection();
             Statement state = conn.createStatement()) {
            state.execute("RUNSCRIPT FROM 'classpath:schema.sql'");
            state.executeUpdate("MERGE INTO TEAMS(ID,NAME,AVATAR_PATH) KEY(ID)" +
                    "VALUES " +
                    "(1,'PuffinClaw','src/main/webapp/resources/images/avatars/Puffin_Ravenclaw_Icon.png')," +
                    "(2,'DeerPuff', 'src/main/webapp/resources/images/avatars/Deer_HufflePuff_Icon.png')," +
                    "(3, 'KittenDor', 'src/main/webapp/resources/images/avatars/Gryffindor_Kitty_Icon.png')," +
                    "(4,'RaptorRin','src/main/webapp/resources/images/avatars/Slytherin_Raptor_Icon.png')"
            );
        }
    }

    @Test
    void findAllTest() throws Exception {
        List<Team> teams = dao.findAll();
        assertEquals(4, teams.size(), "It must be 4 teams");
        assertEquals("PuffinClaw", teams.get(0).getName());
        assertEquals("DeerPuff", teams.get(1).getName());
        assertEquals("KittenDor", teams.get(2).getName());
        assertEquals("RaptorRin", teams.get(3).getName());
    }

    @ParameterizedTest
    @MethodSource("dataCases")
    void findByIdTest(String teamName, int id) throws Exception {
        Optional<Team> team = dao.findById(id);
        assertTrue(team.isPresent(), "It must be a team");
        assertEquals(teamName, team.get().getName());
        assertEquals(id, team.get().getId());
    }

    static Stream<Arguments> dataCases() {
        return Stream.of(
                Arguments.of("PuffinClaw", 1),
                Arguments.of("DeerPuff", 2),
                Arguments.of("KittenDor", 3),
                Arguments.of("RaptorRin", 4)
        );
    }
}
