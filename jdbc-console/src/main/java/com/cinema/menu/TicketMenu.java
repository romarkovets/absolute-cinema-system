package com.cinema.menu;

import com.cinema.dao.*;
import com.cinema.entity.*;
import com.cinema.Main;

import java.sql.SQLException;
import java.util.List;

public class TicketMenu {

    private final TicketDAO ticketDAO = new TicketDAO();
    private final SessionDAO sessionDAO = new SessionDAO();
    private final UserDAO userDAO = new UserDAO();
    private final MovieDAO movieDAO = new MovieDAO();
    private final HallDAO hallDAO = new HallDAO();

    public void showMenu() {
        while (true) {
            printMenu();
            int choice = Main.getIntInput("Выберите действие: ");

            try {
                switch (choice) {
                    case 1:
                        buyTicket();
                        break;
                    case 2:
                        showAllTickets();
                        break;
                    case 3:
                        findTicketById();
                        break;
                    case 4:
                        ticketsByUser();
                        break;
                    case 5:
                        ticketsBySession();
                        break;
                    case 6:
                        returnTicket();
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
        System.out.println("\n--- УПРАВЛЕНИЕ БИЛЕТАМИ ---");
        System.out.println("1. Купить билет");
        System.out.println("2. Показать все билеты");
        System.out.println("3. Найти билет по ID");
        System.out.println("4. Билеты пользователя");
        System.out.println("5. Билеты на сеанс");
        System.out.println("6. Вернуть билет");
        System.out.println("0. Назад");
    }

    private void buyTicket() throws SQLException {
        System.out.println("\n--- ПОКУПКА БИЛЕТА ---");

        List<Session> sessions = sessionDAO.findAll();
        if (sessions.isEmpty()) {
            System.out.println("Нет доступных сеансов");
            return;
        }

        List<User> users = userDAO.findAll();
        if (users.isEmpty()) {
            System.out.println("Сначала добавьте пользователей");
            return;
        }

        System.out.println("\nДоступные сеансы:");
        for (Session s : sessions) {
            Movie movie = movieDAO.findById(s.getMovieId());
            int sold = ticketDAO.countBySessionId(s.getSessionId());
            Hall hall = hallDAO.findById(s.getHallId());
            int totalSeats = hall != null ? hall.getHallType() == 1 ? 120 : hall.getHallType() == 2 ? 30 : 200 : 0;

            System.out.printf("ID: %d | %s | %s %s | Свободно: %d/%d%n",
                    s.getSessionId(),
                    movie != null ? movie.getTitle() : "Неизвестно",
                    s.getSessionDate(),
                    s.getStartTime(),
                    totalSeats - sold,
                    totalSeats);
        }

        int sessionId = Main.getIntInput("\nВыберите ID сеанса: ");

        System.out.println("\nПользователи:");
        for (User u : users) System.out.println(u);
        int userId = Main.getIntInput("Выберите ID пользователя: ");

        int seatNumber = Main.getIntInput("Номер места: ");

        try {
            Ticket ticket = new Ticket(sessionId, userId, seatNumber);
            ticketDAO.create(ticket);
            System.out.println("Билет куплен! Номер билета: " + ticket.getTicketId());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void showAllTickets() throws SQLException {
        List<Ticket> tickets = ticketDAO.findAll();
        System.out.println("\n--- ВСЕ БИЛЕТЫ ---");
        if (tickets.isEmpty()) {
            System.out.println("Билетов нет");
        } else {
            for (Ticket t : tickets) {
                Session session = sessionDAO.findById(t.getSessionId());
                User user = userDAO.findById(t.getUserId());
                System.out.printf("ID: %d | Место: %d | %s | Пользователь: %s%n",
                        t.getTicketId(),
                        t.getSeatNumber(),
                        session != null ? session.getSessionDate() + " " + session.getStartTime() : "Неизвестно",
                        user != null ? user.getFullName() : "Неизвестно");
            }
        }
    }

    private void findTicketById() throws SQLException {
        int id = Main.getIntInput("Введите ID билета: ");
        Ticket ticket = ticketDAO.findById(id);
        if (ticket == null) {
            System.out.println("Билет не найден");
        } else {
            Session session = sessionDAO.findById(ticket.getSessionId());
            User user = userDAO.findById(ticket.getUserId());
            Movie movie = session != null ? movieDAO.findById(session.getMovieId()) : null;

            System.out.println("\n--- ДЕТАЛИ БИЛЕТА ---");
            System.out.println("ID билета: " + ticket.getTicketId());
            System.out.println("Фильм: " + (movie != null ? movie.getTitle() : "Неизвестно"));
            System.out.println("Дата: " + (session != null ? session.getSessionDate() : "Неизвестно"));
            System.out.println("Время: " + (session != null ? session.getStartTime() : "Неизвестно"));
            System.out.println("Место: " + ticket.getSeatNumber());
            System.out.println("Покупатель: " + (user != null ? user.getFullName() : "Неизвестно"));
            System.out.println("Дата покупки: " + ticket.getPurchaseDate());
        }
    }

    private void ticketsByUser() throws SQLException {
        List<User> users = userDAO.findAll();
        if (users.isEmpty()) {
            System.out.println("Нет пользователей");
            return;
        }

        System.out.println("\nПользователи:");
        for (User u : users) System.out.println(u);

        int userId = Main.getIntInput("Выберите ID пользователя: ");
        List<Ticket> tickets = ticketDAO.findByUserId(userId);

        System.out.println("\n--- БИЛЕТЫ ПОЛЬЗОВАТЕЛЯ ---");
        if (tickets.isEmpty()) {
            System.out.println("У пользователя нет билетов");
        } else {
            for (Ticket t : tickets) {
                Session session = sessionDAO.findById(t.getSessionId());
                Movie movie = session != null ? movieDAO.findById(session.getMovieId()) : null;
                System.out.printf("ID: %d | %s | %s %s | Место: %d%n",
                        t.getTicketId(),
                        movie != null ? movie.getTitle() : "Неизвестно",
                        session != null ? session.getSessionDate() : "",
                        session != null ? session.getStartTime() : "",
                        t.getSeatNumber());
            }
        }
    }

    private void ticketsBySession() throws SQLException {
        int sessionId = Main.getIntInput("Введите ID сеанса: ");
        List<Ticket> tickets = ticketDAO.findBySessionId(sessionId);

        System.out.println("\n--- БИЛЕТЫ НА СЕАНС ---");
        if (tickets.isEmpty()) {
            System.out.println("На этот сеанс билеты не проданы");
        } else {
            Session session = sessionDAO.findById(sessionId);
            Movie movie = session != null ? movieDAO.findById(session.getMovieId()) : null;
            System.out.println("Фильм: " + (movie != null ? movie.getTitle() : "Неизвестно"));
            System.out.println("Продано билетов: " + tickets.size());
            System.out.println("Места: ");
            for (Ticket t : tickets) {
                User user = userDAO.findById(t.getUserId());
                System.out.printf("  Место %d - %s%n", t.getSeatNumber(), user != null ? user.getFullName() : "Неизвестно");
            }
        }
    }

    private void returnTicket() throws SQLException {
        int id = Main.getIntInput("Введите ID билета для возврата: ");
        Ticket ticket = ticketDAO.findById(id);

        if (ticket == null) {
            System.out.println("Билет не найден");
            return;
        }

        System.out.print("Вернуть билет? (y/n): ");
        String confirm = Main.getStringInput("");

        if (confirm.equalsIgnoreCase("y")) {
            ticketDAO.delete(id);
            System.out.println("Билет возвращен");
        }
    }
}