package com.cinema.menu;

import com.cinema.dao.*;
import com.cinema.entity.*;
import com.cinema.Main;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class HallMenu {
    private static final Logger LOGGER = Logger.getLogger(HallMenu.class.getName());

    private final HallDAO hallDAO = new HallDAO();

    public void showMenu() {
        while (true) {
            printMenu();
            int choice = Main.getIntInput("Выберите действие: ");

            try {
                switch (choice) {
                    case 1:
                        showAllHalls();
                        break;
                    case 2:
                        findHallById();
                        break;
                    case 3:
                        addHall();
                        break;
                    case 4:
                        updateHall();
                        break;
                    case 5:
                        deleteHall();
                        break;
                    case 0:
                        LOGGER.info("Выход из меню залов");
                        return;
                    default:
                        LOGGER.warning("Неверный выбор в меню залов: " + choice);
                        System.out.println("Неверный выбор!");
                }
            } catch (SQLException e) {
                LOGGER.severe("Ошибка БД в меню залов: " + e.getMessage());
                System.out.println("Ошибка базы данных: " + e.getMessage());
            }

            Main.pressEnterToContinue();
        }
    }

    private void printMenu() {
        System.out.println("\n--- УПРАВЛЕНИЕ ЗАЛАМИ ---");
        System.out.println("1. Показать все залы");
        System.out.println("2. Найти зал по ID");
        System.out.println("3. Добавить зал");
        System.out.println("4. Обновить зал");
        System.out.println("5. Удалить зал");
        System.out.println("0. Назад");
    }

    private void showAllHalls() throws SQLException {
        LOGGER.info("Запрос списка всех залов");
        List<Hall> halls = hallDAO.findAll();
        System.out.println("\n--- СПИСОК ЗАЛОВ ---");

        if (halls.isEmpty()) {
            LOGGER.info("Залов не найдено");
            System.out.println("Залов нет");
        } else {
            LOGGER.info("Найдено залов: " + halls.size());
            for (Hall h : halls) {
                System.out.println(h);
            }
        }
    }

    private void findHallById() throws SQLException {
        int id = Main.getIntInput("Введите ID зала: ");
        LOGGER.info("Поиск зала по ID: " + id);

        Hall hall = hallDAO.findById(id);
        if (hall == null) {
            LOGGER.info("Зал не найден");
            System.out.println("Зал не найден");
        } else {
            LOGGER.info("Зал найден: " + hall.getHallNumber());
            System.out.println("\n" + hall);
        }
    }

    private void addHall() throws SQLException {
        LOGGER.info("Начало добавления нового зала");
        System.out.println("\n--- ДОБАВЛЕНИЕ ЗАЛА ---");

        int hallNumber = Main.getIntInput("Номер зала: ");
        System.out.println("Тип зала:");
        System.out.println("1 - Обычный");
        System.out.println("2 - VIP");
        System.out.println("3 - IMAX");
        int hallType = Main.getIntInput("Выберите тип (1-3): ");

        Hall hall = new Hall(hallNumber, hallType);
        hallDAO.create(hall);

        LOGGER.info("Зал добавлен: номер " + hallNumber);
        System.out.println("Зал добавлен! ID: " + hall.getHallId());
    }

    private void updateHall() throws SQLException {
        int id = Main.getIntInput("Введите ID зала для обновления: ");
        LOGGER.info("Обновление зала ID: " + id);

        Hall hall = hallDAO.findById(id);
        if (hall == null) {
            LOGGER.info("Зал для обновления не найден");
            System.out.println("Зал не найден");
            return;
        }

        System.out.println("Текущие данные: " + hall);
        System.out.println("Оставьте поле пустым, чтобы не менять");

        String numberStr = Main.getOptionalInput("Новый номер зала (" + hall.getHallNumber() + "): ");
        if (!numberStr.isEmpty()) {
            hall.setHallNumber(Integer.parseInt(numberStr));
        }

        String typeStr = Main.getOptionalInput("Новый тип зала (1-3) (" + hall.getHallType() + "): ");
        if (!typeStr.isEmpty()) {
            hall.setHallType(Integer.parseInt(typeStr));
        }

        hallDAO.update(hall);
        LOGGER.info("Зал обновлен");
        System.out.println("Зал обновлен");
    }

    private void deleteHall() throws SQLException {
        int id = Main.getIntInput("Введите ID зала для удаления: ");
        LOGGER.info("Удаление зала ID: " + id);

        System.out.print("Вы уверены? (y/n): ");
        String confirm = Main.getStringInput("");

        if (confirm.equalsIgnoreCase("y")) {
            hallDAO.delete(id);
            LOGGER.info("Зал удален");
            System.out.println("Зал удален");
        } else {
            LOGGER.info("Удаление зала отменено");
        }
    }
}