package com.cinema.menu;

import com.cinema.dao.*;
import com.cinema.entity.*;
import com.cinema.Main;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class SessionMenu {

    private final SessionDAO sessionDAO = new SessionDAO();
    private final MovieDAO movieDAO = new MovieDAO();
    private final HallDAO hallDAO = new HallDAO();
    private final TicketDAO ticketDAO = new TicketDAO();

    public void showMenu() {
        while (true) {
            printMenu();
            int choice = Main.getIntInput("Выберите действие: ");

            try {
                switch (choice) {
                    case 1:
                        showAllSessions();
                        break;
                    case 2:
                        findSessionById();
                        break;
                    case 3:
                        showSessionsByDate();
                        break;
                    case 4:
                        addSession();
                        break;
                    case 5:
                        updateSession();
                        break;
                    case 6:
                        deleteSession();
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
        System.out.println("\n--- УПРАВЛЕНИЕ СЕАНСАМИ ---");
        System.out.println("1. Показать все сеансы");
        System.out.println("2. Найти сеанс по ID");
        System.out.println("3. Показать сеансы на дату");
        System.out.println("4. Добавить сеанс");
        System.out.println("5. Обновить сеанс");
        System.out.println("6. Удалить сеанс");
        System.out.println("0. Назад");
    }

    private void showAllSessions() throws SQLException {
        List<Session> sessions = sessionDAO.findAll();
        System.out.println("\n--- ВСЕ СЕАНСЫ ---");
        if (sessions.isEmpty()) {
            System.out.println("Сеансов нет");
        } else {
            for (Session s : sessions) {
                Movie movie = movieDAO.findById(s.getMovieId());
                Hall hall = hallDAO.findById(s.getHallId());
                int soldTickets = ticketDAO.countBySessionId(s.getSessionId());

                System.out.printf("ID: %d | %s | %s %s | %d руб | Зал %d | Продано: %d%n",
                        s.getSessionId(),
                        movie != null ? movie.getTitle() : "Неизвестно",
                        s.getSessionDate(),
                        s.getStartTime(),
                        (int)s.getPrice(),
                        hall != null ? hall.getHallNumber() : 0,
                        soldTickets);
            }
        }
    }

    private void findSessionById() throws SQLException {
        int id = Main.getIntInput("Введите ID сеанса: ");
        Session session = sessionDAO.findById(id);
        if (session == null) {
            System.out.println("Сеанс не найден");
        } else {
            Movie movie = movieDAO.findById(session.getMovieId());
            Hall hall = hallDAO.findById(session.getHallId());
            int soldTickets = ticketDAO.countBySessionId(id);

            System.out.println("\n--- ДЕТАЛИ СЕАНСА ---");
            System.out.println("ID: " + session.getSessionId());
            System.out.println("Фильм: " + (movie != null ? movie.getTitle() : "Неизвестно"));
            System.out.println("Дата: " + session.getSessionDate());
            System.out.println("Время: " + session.getStartTime());
            System.out.println("Зал: " + (hall != null ? hall.getHallNumber() : 0));
            System.out.println("Цена: " + (int)session.getPrice() + " руб");
            System.out.println("Продано билетов: " + soldTickets);
        }
    }

    private void showSessionsByDate() throws SQLException {
        System.out.print("Введите дату (ГГГГ-ММ-ДД): ");
        String dateStr = Main.getStringInput("");

        try {
            LocalDate date = LocalDate.parse(dateStr);
            List<Session> sessions = sessionDAO.findByDate(date);

            System.out.println("\n--- СЕАНСЫ НА " + date + " ---");
            if (sessions.isEmpty()) {
                System.out.println("Сеансов нет");
            } else {
                for (Session s : sessions) {
                    Movie movie = movieDAO.findById(s.getMovieId());
                    Hall hall = hallDAO.findById(s.getHallId());
                    System.out.printf("%s | %s | %d руб | Зал %d%n",
                            s.getStartTime(),
                            movie != null ? movie.getTitle() : "Неизвестно",
                            (int)s.getPrice(),
                            hall != null ? hall.getHallNumber() : 0);
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты");
        }
    }

    private void addSession() throws SQLException {
        System.out.println("\n--- ДОБАВЛЕНИЕ СЕАНСА ---");

        List<Movie> movies = movieDAO.findAll();
        if (movies.isEmpty()) {
            System.out.println("Сначала добавьте фильмы");
            return;
        }

        List<Hall> halls = hallDAO.findAll();
        if (halls.isEmpty()) {
            System.out.println("Сначала добавьте залы");
            return;
        }

        System.out.println("\nФильмы:");
        for (Movie m : movies) System.out.println(m);
        int movieId = Main.getIntInput("Выберите ID фильма: ");

        System.out.println("\nЗалы:");
        for (Hall h : halls) System.out.println(h);
        int hallId = Main.getIntInput("Выберите ID зала: ");

        System.out.print("Дата (ГГГГ-ММ-ДД): ");
        LocalDate date = LocalDate.parse(Main.getStringInput(""));

        System.out.print("Время (ЧЧ:ММ:СС): ");
        LocalTime time = LocalTime.parse(Main.getStringInput(""));

        int price = Main.getIntInput("Цена (руб): ");

        Session session = new Session(movieId, hallId, date, time, price);
        sessionDAO.create(session);
        System.out.println("Сеанс добавлен! ID: " + session.getSessionId());
    }

    private void updateSession() throws SQLException {
        int id = Main.getIntInput("Введите ID сеанса для обновления: ");
        Session session = sessionDAO.findById(id);

        if (session == null) {
            System.out.println("Сеанс не найден");
            return;
        }

        System.out.println("Текущие данные сеанса ID: " + id);
        System.out.println("Оставьте поле пустым, чтобы не менять");

        String dateStr = Main.getOptionalInput("Новая дата (" + session.getSessionDate() + "): ");
        if (!dateStr.isEmpty()) session.setSessionDate(LocalDate.parse(dateStr));

        String timeStr = Main.getOptionalInput("Новое время (" + session.getStartTime() + "): ");
        if (!timeStr.isEmpty()) session.setStartTime(LocalTime.parse(timeStr));

        String priceStr = Main.getOptionalInput("Новая цена (" + (int)session.getPrice() + " руб): ");
        if (!priceStr.isEmpty()) session.setPrice(Double.parseDouble(priceStr));

        sessionDAO.update(session);
        System.out.println("Сеанс обновлен");
    }

    private void deleteSession() throws SQLException {
        int id = Main.getIntInput("Введите ID сеанса для удаления: ");

        System.out.print("Вы уверены? (y/n): ");
        String confirm = Main.getStringInput("");

        if (confirm.equalsIgnoreCase("y")) {
            sessionDAO.delete(id);
            System.out.println("Сеанс удален");
        }
    }
}