package com.cinema.menu;

import com.cinema.dao.*;
import com.cinema.entity.*;
import com.cinema.Main;

import java.sql.SQLException;
import java.util.List;

public class UserMenu {

    private final UserDAO userDAO = new UserDAO();

    public void showMenu() {
        while (true) {
            printMenu();
            int choice = Main.getIntInput("Выберите действие: ");

            try {
                switch (choice) {
                    case 1:
                        showAllUsers();
                        break;
                    case 2:
                        findUserById();
                        break;
                    case 3:
                        addUser();
                        break;
                    case 4:
                        updateUser();
                        break;
                    case 5:
                        deleteUser();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Неверный выбор!");
                }
            } catch (SQLException e) {
                System.out.println("Ошибка базы данных: " + e.getMessage());
            }

            Main.pressEnterToContinue();
        }
    }

    private void printMenu() {
        System.out.println("\n--- УПРАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯМИ ---");
        System.out.println("1. Показать всех пользователей");
        System.out.println("2. Найти пользователя по ID");
        System.out.println("3. Добавить пользователя");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Назад");
    }

    private void showAllUsers() throws SQLException {
        List<User> users = userDAO.findAll();
        System.out.println("\n--- СПИСОК ПОЛЬЗОВАТЕЛЕЙ ---");
        if (users.isEmpty()) {
            System.out.println("Пользователей нет");
        } else {
            for (User u : users) {
                System.out.println(u);
            }
        }
    }

    private void findUserById() throws SQLException {
        int id = Main.getIntInput("Введите ID пользователя: ");
        User user = userDAO.findById(id);
        if (user == null) {
            System.out.println("Пользователь не найден");
        } else {
            System.out.println("\n" + user);
            System.out.println("Паспорт: " + user.getPassport());
            System.out.println("Телефон: " + user.getPhone());
        }
    }

    private void addUser() throws SQLException {
        System.out.println("\n--- ДОБАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ ---");
        String email = Main.getStringInput("Email: ");
        String fullName = Main.getStringInput("Полное имя: ");
        String passport = Main.getStringInput("Паспорт (9 цифр): ");
        String phone = Main.getStringInput("Телефон: ");

        User user = new User(email, fullName, passport, phone);
        userDAO.create(user);
        System.out.println("Пользователь добавлен! ID: " + user.getUserId());
    }

    private void updateUser() throws SQLException {
        int id = Main.getIntInput("Введите ID пользователя для обновления: ");
        User user = userDAO.findById(id);

        if (user == null) {
            System.out.println("Пользователь не найден");
            return;
        }

        System.out.println("Текущие данные: " + user);
        System.out.println("Оставьте поле пустым, чтобы не менять");

        String email = Main.getOptionalInput("Новый email (" + user.getEmail() + "): ");
        if (!email.isEmpty()) user.setEmail(email);

        String name = Main.getOptionalInput("Новое имя (" + user.getFullName() + "): ");
        if (!name.isEmpty()) user.setFullName(name);

        String passport = Main.getOptionalInput("Новый паспорт (" + user.getPassport() + "): ");
        if (!passport.isEmpty()) user.setPassport(passport);

        String phone = Main.getOptionalInput("Новый телефон (" + user.getPhone() + "): ");
        if (!phone.isEmpty()) user.setPhone(phone);

        userDAO.update(user);
        System.out.println("Пользователь обновлен");
    }

    private void deleteUser() throws SQLException {
        int id = Main.getIntInput("Введите ID пользователя для удаления: ");

        System.out.print("Вы уверены? (y/n): ");
        String confirm = Main.getStringInput("");

        if (confirm.equalsIgnoreCase("y")) {
            userDAO.delete(id);
            System.out.println("Пользователь удален");
        }
    }
}