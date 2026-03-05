package com.cinema.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseUtil {
    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream input = DatabaseUtil.class.getClassLoader()
                .getResourceAsStream("application.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            url = prop.getProperty("db.url");
            username = prop.getProperty("db.username");
            password = prop.getProperty("db.password");

            Class.forName("org.postgresql.Driver");
            System.out.println("JDBC Driver загружен успешно");
        } catch (Exception e) {
            System.out.println("Ошибка загрузки конфигурации БД: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Тест подключения к БД успешен");
            return true;
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к БД: " + e.getMessage());
            return false;
        }
    }
}