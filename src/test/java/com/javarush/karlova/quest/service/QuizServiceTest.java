package com.javarush.karlova.quest.service;

import com.javarush.karlova.quest.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.javarush.karlova.quest.model.Level.EASY;
import static com.javarush.karlova.quest.model.Role.STUDENT;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({QuizServiceResolver.class})

public class QuizServiceTest {

    private QuizState state;
    @Nested
    class WithGeneralStartedData {
        @BeforeEach
        void setUp(QuizService quizService) {
            //Start conditions
            User randomUser = new User(null, "Malfoy", STUDENT, null);
            TestEntity randomTest = new TestEntity(1L, "Astronomy", EASY, "ru");
            state = quizService.startQuiz(randomUser, randomTest);
        }
        @Test
        @DisplayName("Test incrementing of the index and storing of the answer ")
        void submitAnswerTest(QuizService quizService) {

            assertEquals(0, state.getCurrentQuestionIndex());
            assertTrue(state.getAnswers().isEmpty());
            //User choose variant 2
            quizService.submitAnswer(state, 2);
            assertEquals(1, state.getCurrentQuestionIndex());
            assertEquals(List.of(2), state.getAnswers().get(0));
        }

        @Test
        void canRestartQuizWhileAliveTest(QuizService quizService) {
            assertTrue(quizService.canRestartQuiz(state));
            state.decrementLife();//2
            assertTrue(quizService.canRestartQuiz(state));
            state.decrementLife();//1
            assertTrue(quizService.canRestartQuiz(state));
            state.decrementLife();//0
            assertFalse(quizService.canRestartQuiz(state));
        }

        @Test
        @DisplayName("Test parameters after the quiz resets: decrement lives, index, clearing of answers")
        void restartQuizTest(QuizService quizService) {
            state.setCurrentQuestionIndex(7);
            state.getAnswers().put(0, List.of(3));
            state.getAnswers().put(1, List.of(1));
            state.getAnswers().put(2, List.of(2));

            quizService.restartQuiz(state);

            assertEquals(2, state.getRemainingLives());
            assertEquals(0, state.getCurrentQuestionIndex());
            assertTrue(state.getAnswers().isEmpty());
        }
        @Test
        @DisplayName("Test loading questions from JSON")
        void questionsAreLoadedTest() {
           assertNotNull(state.getQuestions());
           assertEquals(40, state.getQuestions().size(), "java_easy contains 40 questions");
           assertEquals(0, state.getCurrentQuestionIndex());
        }
    }
    @Nested
    class WithOwnDataCases {
        @ParameterizedTest
        @DisplayName("Test initializing of quiz with the different data cases")
        @CsvFileSource(resources = "/quizServiceCases", numLinesToSkip = 1)
        void startQuizConditionsTest(String nickname, String role, int teamId, long testId,
                                     String title, String level, String language,
                                     int expectedLives, QuizService quizService) {
            Team team = new Team(teamId, null, null);
            User user = new User(null, nickname, Role.valueOf(role), team);
            TestEntity test = new TestEntity(testId, title, Level.valueOf(level), language);

            state = quizService.startQuiz(user, test);
            assertEquals(testId, state.getTestId());
            assertEquals(expectedLives, state.getRemainingLives());
            assertEquals(0, state.getCurrentQuestionIndex());
            assertTrue(state.getAnswers().isEmpty());
        }
    }
}
