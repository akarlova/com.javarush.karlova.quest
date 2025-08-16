package com.javarush.karlova.quest.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final Properties properties = new Properties();

    static {
        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("db.properties not found");
            }
            properties.load(in);
            Class.forName("org.h2.Driver");
        } catch (Exception e) {
            throw new ExceptionInInitializerError("DB connection initialization error" + e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.user");
        String pass = properties.getProperty("jdbc.pass");
        Connection conn =DriverManager.getConnection(url, user, pass);
//        System.out.println(">>> URL from props = " + properties.getProperty("jdbc.url"));
//        System.out.println(">>> H2 URL = " + conn.getMetaData().getURL());
        conn.setAutoCommit(true);
        //System.out.println("Current working dir: " + new java.io.File(".").getAbsolutePath());
        //System.out.println("URL: " + conn.getMetaData().getURL());
        return conn;
    }

}
