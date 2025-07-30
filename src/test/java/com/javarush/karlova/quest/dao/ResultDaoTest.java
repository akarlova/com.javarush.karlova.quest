package com.javarush.karlova.quest.dao;

import com.javarush.karlova.quest.model.Result;
import com.javarush.karlova.quest.util.DBConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Statement;

public class ResultDaoTest {
    private static ResultDao dao;
    @BeforeAll
    static void init() throws Exception {
        dao = new ResultDao();
        try(Connection conn = DBConnection.getConnection();
        Statement state = conn.createStatement() ) {
            state.execute("RUNSCRIPT FROM 'classpath:schema.sql'");
            state.executeUpdate("DELETE FROM results");
            state.executeUpdate("MERGE INTO USERS (ID,NICKNAME,ROLE,TEAM_ID) KEY (ID) VALUES ( " +
                    "10, 'testUser','STUDENT',1)");

        }
    }
    @Test
    void saveResultTest() throws Exception {
        Result result = new Result(10L,2L, 20,1200);
        Result savedResult = dao.saveResult(result);

        assertNotNull(savedResult);
        assertEquals(10L, savedResult.getUserId());
        assertEquals(2L, savedResult.getTestId());
        assertEquals(20, savedResult.getScore());
        assertTrue(savedResult.getDurationSeconds() >=1200);

    }
}
