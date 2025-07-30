package com.javarush.karlova.quest.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javarush.karlova.quest.model.Question;
import com.javarush.karlova.quest.model.TestEntity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestLoader {
    private static final Map<Long, TestEntity> tests = new HashMap<>();

    static {
        Gson gson = new Gson();
        String[] files = {"java_easy.json", "patterns_easy.json","test_5.json",
        "java_core_hard.json", "spring_quiz.json"};
        for (String file : files) {
            try (InputStream inputStream = TestLoader.class.getClassLoader()
                    .getResourceAsStream("tests/" + file)) {
                if (inputStream == null) {
                    System.err.println(file + " not found");
                    continue;
                }
                Type type = new TypeToken<TestEntity>() {
                }.getType();
                TestEntity test = gson.fromJson(new InputStreamReader(inputStream, "UTF-8"), type);

                for(Question question : test.getQuestions()) {
                    List<String> cleanedOptions = question.getOptions().stream()
                            .filter(opt->opt != null && !opt.isBlank())
                            .collect(Collectors.toList());
                    question.setOptions(cleanedOptions);
                }

                tests.put(test.getId(), test);
            } catch (Exception e) {
                throw new ExceptionInInitializerError("Download failed for " + file + ": " + e);
            }
        }
    }

    public static TestEntity getTest(long id) {
        return tests.get(id);
    }

    public static Map<Long, TestEntity> getAllTests() {
        return Map.copyOf(tests);
    }
}

