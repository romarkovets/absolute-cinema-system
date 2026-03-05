package com.cinema;

import com.cinema.menu.*;
import com.cinema.util.DatabaseUtil;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final MovieMenu movieMenu = new MovieMenu();
    private static final ActorMenu actorMenu = new ActorMenu();
    private static final SessionMenu sessionMenu = new SessionMenu();
    private static final TicketMenu ticketMenu = new TicketMenu();
    private static final UserMenu userMenu = new UserMenu();

    public static void main(String[] args) {
        System.out.println("Запуск приложения Кинотеатр");

        if (!DatabaseUtil.testConnection()) {
            System.out.println("\n❌ Ошибка: Не удалось подключиться к базе данных.");
            System.out.println("Проверьте параметры подключения в application.properties");
            return;
        }

        System.out.println("Подключение к БД успешно установлено");

        while (true) {
            printMainMenu();
            int choice = getIntInput("Выберите раздел: ");

            switch (choice) {
                case 1:
                    movieMenu.showMenu();
                    break;
                case 2:
                    actorMenu.showMenu();
                    break;
                case 3:
                    sessionMenu.showMenu();
                    break;
                case 4:
                    ticketMenu.showMenu();
                    break;
                case 5:
                    userMenu.showMenu();
                    break;
                case 0:
                    System.out.println("До свидания!");
                    return;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n=======================================");
        System.out.println("    КИНОТЕАТР - СИСТЕМА УПРАВЛЕНИЯ");
        System.out.println("=======================================");
        System.out.println("1. Фильмы");
        System.out.println("2. Актеры");
        System.out.println("3. Сеансы");
        System.out.println("4. Билеты");
        System.out.println("5. Пользователи");
        System.out.println("0. Выход");
    }

    public static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
            }
        }
    }

    public static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static String getOptionalInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static void pressEnterToContinue() {
        System.out.println("\nНажмите Enter для продолжения...");
        scanner.nextLine();
    }

    public static Scanner getScanner() {
        return scanner;
    }
}