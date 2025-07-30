package com.javarush.karlova.quest.service;

import com.javarush.karlova.quest.model.*;
import com.javarush.karlova.quest.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

@ExtendWith({QuizServiceResolver.class})
public class QuizServiceFinishTest {

    private QuizState state;
    private User user;

    @BeforeEach
    void setUp() throws Exception {
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("RUNSCRIPT FROM 'classpath:schema.sql'");
            statement.executeUpdate("DELETE FROM RESULTS");
            statement.executeUpdate("MERGE INTO USERS(ID,NICKNAME,ROLE,TEAM_ID) KEY (ID) VALUES (" +
                    "7, 'Oliver Wood','STUDENT',3)");
        }

        Question q1 = new Question("Q1", List.of("a", "b", "c", "d"), List.of(1));
        Question q2 = new Question("Q2", List.of("a", "b", "c", "d"), List.of(0, 2));
        Question q3 = new Question("Q3", List.of("a", "b", "c", "d"), List.of(0));

        List<Question> questions = List.of(q1, q2, q3);
        user = new User(7L, "Oliver Wood", Role.STUDENT, new Team(3, null, null));
        state = new QuizState(3L, 2, questions);

        state.getAnswers().put(0, List.of(1));
        state.getAnswers().put(1, List.of(0, 1));
        state.getAnswers().put(2, List.of(0));

        Field f = QuizState.class.getDeclaredField("startTimeMillis");
        f.setAccessible(true);
        f.set(state, System.currentTimeMillis() - 360_000L);
    }

    @Test
    void finishQuizSubmitsAndReturnsResultTest(QuizService service) throws Exception {
        Result result = service.finishQuiz(user, state);
        assertNotNull(result.getId());
        assertEquals(7L, result.getUserId());
        assertEquals(3L, result.getTestId());
        assertEquals(2, result.getScore());
        assertTrue(result.getDurationSeconds() >= 360);
        assertNotNull(result.getTakenAt());
    }

    @Test
    void finishQuizWhenTimeOutTest(QuizService service) throws Exception {
        Field f = QuizState.class.getDeclaredField("startTimeMillis");
        f.setAccessible(true);
        f.set(state, System.currentTimeMillis() - 2 * 3_600_000L);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.finishQuiz(user, state));
        assertEquals("Time's up", ex.getMessage());
    }
}
