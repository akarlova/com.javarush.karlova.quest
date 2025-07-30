package com.javarush.karlova.quest.util;

import com.javarush.karlova.quest.model.TestEntity;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
public class TestLoaderTest {

    @Test
    void loadAllTestsTest() {
        Map<Long, TestEntity> tests = TestLoader.getAllTests();

        assertEquals(5, tests.size());

        assertTrue(tests.containsKey(1L));
        assertTrue(tests.containsKey(2L));

        TestEntity javaEasy = tests.get(1L);
        assertNotNull(javaEasy);
        assertEquals("Java ТехТест", javaEasy.getTitle());
        assertEquals(40, javaEasy.getQuestions().size());

        TestEntity patternsEasy = tests.get(2L);
        assertNotNull(patternsEasy);
        assertEquals("Design Patterns", patternsEasy.getTitle());
        assertTrue(patternsEasy.getQuestions().size() > 20);

        //TODO for hard test the same
    }
    @Test
    void getTestByIdTest() {
        TestEntity t1= TestLoader.getTest(1L);
        TestEntity t2= TestLoader.getTest(2L);

        assertNotNull(t1);
        assertNotNull(t2);
        assertNull(TestLoader.getTest(99L));
    }
}
