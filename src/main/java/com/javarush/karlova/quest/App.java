package com.javarush.karlova.quest;

import com.javarush.karlova.quest.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("OK, схемы созданы, коннект успешен!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
