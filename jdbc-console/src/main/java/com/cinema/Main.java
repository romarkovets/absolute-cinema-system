package com.cinema;

import com.cinema.dao.*;
import com.cinema.entity.*;
import com.cinema.util.DatabaseUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final MovieDAO movieDAO = new MovieDAO();
    private static final ActorDAO actorDAO = new ActorDAO();
    private static final MovieActorDAO movieActorDAO = new MovieActorDAO();
    private static final HallDAO hallDAO = new HallDAO();
    private static final SessionDAO sessionDAO = new SessionDAO();
    private static final UserDAO userDAO = new UserDAO();
    private static final TicketDAO ticketDAO = new TicketDAO();

    public static void main(String[] args) {
        while (true) {
            printMainMenu();
            int choice = getIntInput("Выберите раздел: ");

            switch (choice) {
                case 1:
                    moviesMenu();
                    break;
                case 2:
                    actorsMenu();
                    break;
                case 3:
                    sessionsMenu();
                    break;
                case 4:
                    ticketsMenu();
                    break;
                case 5:
                    usersMenu();
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

    private static void moviesMenu() {
        while (true) {
            System.out.println("\n--- УПРАВЛЕНИЕ ФИЛЬМАМИ ---");
            System.out.println("1. Показать все фильмы");
            System.out.println("2. Найти фильм по ID");
            System.out.println("3. Добавить фильм");
            System.out.println("4. Обновить фильм");
            System.out.println("5. Удалить фильм");
            System.out.println("0. Назад");

            int choice = getIntInput("Выберите действие: ");

            try {
                switch (choice) {
                    case 1:
                        showAllMovies();
                        break;
                    case 2:
                        findMovieById();
                        break;
                    case 3:
                        addMovie();
                        break;
                    case 4:
                        updateMovie();
                        break;
                    case 5:
                        deleteMovie();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Неверный выбор!");
                }
            } catch (SQLException e) {
                System.out.println("Ошибка базы данных: " + e.getMessage());
            }

            pressEnterToContinue();
        }
    }

    private static void showAllMovies() throws SQLException {
        List<Movie> movies = movieDAO.findAll();
        System.out.println("\n--- СПИСОК ФИЛЬМОВ ---");
        if (movies.isEmpty()) {
            System.out.println("Фильмов нет");
        } else {
            for (Movie m : movies) {
                System.out.println(m);

                List<MovieActor> movieActors = movieActorDAO.findByMovieId(m.getMovieId());
                if (!movieActors.isEmpty()) {
                    System.out.print("  Актеры: ");
                    for (MovieActor ma : movieActors) {
                        Actor actor = actorDAO.findById(ma.getActorId());
                        if (actor != null) {
                            System.out.print(actor.getFullName() + " (" + ma.getCharacterName() + "), ");
                        }
                    }
                    System.out.println();
                }
            }
        }
    }

    private static void findMovieById() throws SQLException {
        int id = getIntInput("Введите ID фильма: ");
        Movie movie = movieDAO.findById(id);
        if (movie == null) {
            System.out.println("Фильм не найден");
        } else {
            System.out.println("\n" + movie);
            System.out.println("Описание: " + movie.getDescription());

            List<MovieActor> movieActors = movieActorDAO.findByMovieId(id);
            if (!movieActors.isEmpty()) {
                System.out.println("\nАктеры в фильме:");
                for (MovieActor ma : movieActors) {
                    Actor actor = actorDAO.findById(ma.getActorId());
                    if (actor != null) {
                        System.out.println("  " + actor.getFullName() + " - " + ma.getCharacterName());
                    }
                }
            }
        }
    }

    private static void addMovie() throws SQLException {
        System.out.println("\n--- ДОБАВЛЕНИЕ ФИЛЬМА ---");
        System.out.print("Название: ");
        String title = scanner.nextLine();

        System.out.print("Описание: ");
        String description = scanner.nextLine();

        int duration = getIntInput("Длительность (мин): ");
        int year = getIntInput("Год выпуска: ");

        Movie movie = new Movie(title, description, duration, year);
        movieDAO.create(movie);
        System.out.println("Фильм добавлен! ID: " + movie.getMovieId());
    }

    private static void updateMovie() throws SQLException {
        int id = getIntInput("Введите ID фильма для обновления: ");
        Movie movie = movieDAO.findById(id);

        if (movie == null) {
            System.out.println("Фильм не найден");
            return;
        }

        System.out.println("Текущие данные: " + movie);
        System.out.println("Оставьте поле пустым, чтобы не менять");

        System.out.print("Новое название (" + movie.getTitle() + "): ");
        String title = scanner.nextLine();
        if (!title.isEmpty()) {
            movie.setTitle(title);
        }

        System.out.print("Новое описание: ");
        String desc = scanner.nextLine();
        if (!desc.isEmpty()) {
            movie.setDescription(desc);
        }

        String input;
        System.out.print("Новая длительность (" + movie.getDurationMinutes() + "): ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            movie.setDurationMinutes(Integer.parseInt(input));
        }

        System.out.print("Новый год (" + movie.getReleaseYear() + "): ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            movie.setReleaseYear(Integer.parseInt(input));
        }

        movieDAO.update(movie);
        System.out.println("Фильм обновлен");
    }

    private static void deleteMovie() throws SQLException {
        int id = getIntInput("Введите ID фильма для удаления: ");

        System.out.print("Вы уверены? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            movieActorDAO.deleteByMovieId(id);
            movieDAO.delete(id);
            System.out.println("Фильм удален");
        }
    }

    private static void actorsMenu() {
        while (true) {
            System.out.println("\n--- УПРАВЛЕНИЕ АКТЕРАМИ ---");
            System.out.println("1. Показать всех актеров");
            System.out.println("2. Найти актера по ID");
            System.out.println("3. Добавить актера");
            System.out.println("4. Обновить актера");
            System.out.println("5. Удалить актера");
            System.out.println("6. Связать актера с фильмом");
            System.out.println("0. Назад");

            int choice = getIntInput("Выберите действие: ");

            try {
                switch (choice) {
                    case 1:
                        showAllActors();
                        break;
                    case 2:
                        findActorById();
                        break;
                    case 3:
                        addActor();
                        break;
                    case 4:
                        updateActor();
                        break;
                    case 5:
                        deleteActor();
                        break;
                    case 6:
                        linkActorToMovie();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Неверный выбор!");
                }
            } catch (SQLException e) {
                System.out.println("Ошибка базы данных: " + e.getMessage());
            }

            pressEnterToContinue();
        }
    }

    private static void showAllActors() throws SQLException {
        List<Actor> actors = actorDAO.findAll();
        System.out.println("\n--- СПИСОК АКТЕРОВ ---");
        if (actors.isEmpty()) {
            System.out.println("Актеров нет");
        } else {
            for (Actor a : actors) {
                System.out.println(a);
            }
        }
    }

    private static void findActorById() throws SQLException {
        int id = getIntInput("Введите ID актера: ");
        Actor actor = actorDAO.findById(id);
        if (actor == null) {
            System.out.println("Актер не найден");
        } else {
            System.out.println("\n" + actor);

            List<MovieActor> movieActors = movieActorDAO.findByActorId(id);
            if (!movieActors.isEmpty()) {
                System.out.println("\nФильмы с участием:");
                for (MovieActor ma : movieActors) {
                    Movie movie = movieDAO.findById(ma.getMovieId());
                    if (movie != null) {
                        System.out.println("  " + movie.getTitle() + " - " + ma.getCharacterName());
                    }
                }
            }
        }
    }

    private static void addActor() throws SQLException {
        System.out.println("\n--- ДОБАВЛЕНИЕ АКТЕРА ---");
        System.out.print("Полное имя: ");
        String fullName = scanner.nextLine();

        System.out.print("Страна: ");
        String country = scanner.nextLine();

        Actor actor = new Actor(fullName, country);
        actorDAO.create(actor);
        System.out.println("Актер добавлен! ID: " + actor.getActorId());
    }

    private static void updateActor() throws SQLException {
        int id = getIntInput("Введите ID актера для обновления: ");
        Actor actor = actorDAO.findById(id);

        if (actor == null) {
            System.out.println("Актер не найден");
            return;
        }

        System.out.println("Текущие данные: " + actor);
        System.out.println("Оставьте поле пустым, чтобы не менять");

        System.out.print("Новое имя (" + actor.getFullName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            actor.setFullName(name);
        }

        System.out.print("Новая страна (" + actor.getCountry() + "): ");
        String country = scanner.nextLine();
        if (!country.isEmpty()) {
            actor.setCountry(country);
        }

        actorDAO.update(actor);
        System.out.println("Актер обновлен");
    }

    private static void deleteActor() throws SQLException {
        int id = getIntInput("Введите ID актера для удаления: ");

        System.out.print("Вы уверены? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            actorDAO.delete(id);
            System.out.println("Актер удален");
        }
    }

    private static void linkActorToMovie() throws SQLException {
        System.out.println("\n--- СВЯЗЬ АКТЕРА С ФИЛЬМОМ ---");

        List<Movie> movies = movieDAO.findAll();
        if (movies.isEmpty()) {
            System.out.println("Сначала добавьте фильмы");
            return;
        }

        List<Actor> actors = actorDAO.findAll();
        if (actors.isEmpty()) {
            System.out.println("Сначала добавьте актеров");
            return;
        }

        System.out.println("\nФильмы:");
        for (Movie m : movies) {
            System.out.println(m);
        }
        int movieId = getIntInput("Выберите ID фильма: ");

        System.out.println("\nАктеры:");
        for (Actor a : actors) {
            System.out.println(a);
        }
        int actorId = getIntInput("Выберите ID актера: ");

        System.out.print("Имя персонажа: ");
        String characterName = scanner.nextLine();

        MovieActor movieActor = new MovieActor(movieId, actorId, characterName);
        movieActorDAO.create(movieActor);
        System.out.println("Связь создана!");
    }

    private static void sessionsMenu() {
        while (true) {
            System.out.println("\n--- УПРАВЛЕНИЕ СЕАНСАМИ ---");
            System.out.println("1. Показать все сеансы");
            System.out.println("2. Найти сеанс по ID");
            System.out.println("3. Показать сеансы на дату");
            System.out.println("4. Добавить сеанс");
            System.out.println("5. Обновить сеанс");
            System.out.println("6. Удалить сеанс");
            System.out.println("0. Назад");

            int choice = getIntInput("Выберите действие: ");

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

            pressEnterToContinue();
        }
    }

    private static void showAllSessions() throws SQLException {
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

    private static void findSessionById() throws SQLException {
        int id = getIntInput("Введите ID сеанса: ");
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

    private static void showSessionsByDate() throws SQLException {
        System.out.print("Введите дату (ГГГГ-ММ-ДД): ");
        String dateStr = scanner.nextLine();

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

    private static void addSession() throws SQLException {
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
        for (Movie m : movies) {
            System.out.println(m);
        }
        int movieId = getIntInput("Выберите ID фильма: ");

        System.out.println("\nЗалы:");
        for (Hall h : halls) {
            System.out.println(h);
        }
        int hallId = getIntInput("Выберите ID зала: ");

        System.out.print("Дата (ГГГГ-ММ-ДД): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.print("Время (ЧЧ:ММ:СС): ");
        LocalTime time = LocalTime.parse(scanner.nextLine());

        int price = getIntInput("Цена (руб): ");

        Session session = new Session(movieId, hallId, date, time, price);
        sessionDAO.create(session);
        System.out.println("Сеанс добавлен! ID: " + session.getSessionId());
    }

    private static void updateSession() throws SQLException {
        int id = getIntInput("Введите ID сеанса для обновления: ");
        Session session = sessionDAO.findById(id);

        if (session == null) {
            System.out.println("Сеанс не найден");
            return;
        }

        System.out.println("Текущие данные сеанса ID: " + id);
        System.out.println("Оставьте поле пустым, чтобы не менять");

        System.out.print("Новая дата (" + session.getSessionDate() + "): ");
        String dateStr = scanner.nextLine();
        if (!dateStr.isEmpty()) {
            session.setSessionDate(LocalDate.parse(dateStr));
        }

        System.out.print("Новое время (" + session.getStartTime() + "): ");
        String timeStr = scanner.nextLine();
        if (!timeStr.isEmpty()) {
            session.setStartTime(LocalTime.parse(timeStr));
        }

        String priceStr = getOptionalInput("Новая цена (" + (int)session.getPrice() + " руб): ");
        if (!priceStr.isEmpty()) {
            session.setPrice(Double.parseDouble(priceStr));
        }

        sessionDAO.update(session);
        System.out.println("Сеанс обновлен");
    }

    private static void deleteSession() throws SQLException {
        int id = getIntInput("Введите ID сеанса для удаления: ");

        System.out.print("Вы уверены? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            sessionDAO.delete(id);
            System.out.println("Сеанс удален");
        }
    }

    private static void ticketsMenu() {
        while (true) {
            System.out.println("\n--- УПРАВЛЕНИЕ БИЛЕТАМИ ---");
            System.out.println("1. Купить билет");
            System.out.println("2. Показать все билеты");
            System.out.println("3. Найти билет по ID");
            System.out.println("4. Билеты пользователя");
            System.out.println("5. Билеты на сеанс");
            System.out.println("6. Вернуть билет");
            System.out.println("0. Назад");

            int choice = getIntInput("Выберите действие: ");

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

            pressEnterToContinue();
        }
    }

    private static void buyTicket() throws SQLException {
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

        int sessionId = getIntInput("\nВыберите ID сеанса: ");

        System.out.println("\nПользователи:");
        for (User u : users) {
            System.out.println(u);
        }
        int userId = getIntInput("Выберите ID пользователя: ");

        int seatNumber = getIntInput("Номер места: ");

        Ticket ticket = new Ticket(sessionId, userId, seatNumber);
        ticketDAO.create(ticket);
        System.out.println("Билет куплен! Номер билета: " + ticket.getTicketId());
    }

    private static void showAllTickets() throws SQLException {
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

    private static void findTicketById() throws SQLException {
        int id = getIntInput("Введите ID билета: ");
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

    private static void ticketsByUser() throws SQLException {
        List<User> users = userDAO.findAll();
        if (users.isEmpty()) {
            System.out.println("Нет пользователей");
            return;
        }

        System.out.println("\nПользователи:");
        for (User u : users) {
            System.out.println(u);
        }

        int userId = getIntInput("Выберите ID пользователя: ");
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

    private static void ticketsBySession() throws SQLException {
        int sessionId = getIntInput("Введите ID сеанса: ");
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

    private static void returnTicket() throws SQLException {
        int id = getIntInput("Введите ID билета для возврата: ");
        Ticket ticket = ticketDAO.findById(id);

        if (ticket == null) {
            System.out.println("Билет не найден");
            return;
        }

        System.out.print("Вернуть билет? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            ticketDAO.delete(id);
            System.out.println("Билет возвращен");
        }
    }

    private static void usersMenu() {
        while (true) {
            System.out.println("\n--- УПРАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯМИ ---");
            System.out.println("1. Показать всех пользователей");
            System.out.println("2. Найти пользователя по ID");
            System.out.println("3. Добавить пользователя");
            System.out.println("4. Обновить пользователя");
            System.out.println("5. Удалить пользователя");
            System.out.println("0. Назад");

            int choice = getIntInput("Выберите действие: ");

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

            pressEnterToContinue();
        }
    }

    private static void showAllUsers() throws SQLException {
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

    private static void findUserById() throws SQLException {
        int id = getIntInput("Введите ID пользователя: ");
        User user = userDAO.findById(id);
        if (user == null) {
            System.out.println("Пользователь не найден");
        } else {
            System.out.println("\n" + user);
            System.out.println("Паспорт: " + user.getPassport());
            System.out.println("Телефон: " + user.getPhone());
        }
    }

    private static void addUser() throws SQLException {
        System.out.println("\n--- ДОБАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Полное имя: ");
        String fullName = scanner.nextLine();

        System.out.print("Паспорт (9 цифр): ");
        String passport = scanner.nextLine();

        System.out.print("Телефон: ");
        String phone = scanner.nextLine();

        User user = new User(email, fullName, passport, phone);
        userDAO.create(user);
        System.out.println("Пользователь добавлен! ID: " + user.getUserId());
    }

    private static void updateUser() throws SQLException {
        int id = getIntInput("Введите ID пользователя для обновления: ");
        User user = userDAO.findById(id);

        if (user == null) {
            System.out.println("Пользователь не найден");
            return;
        }

        System.out.println("Текущие данные: " + user);
        System.out.println("Оставьте поле пустым, чтобы не менять");

        System.out.print("Новый email (" + user.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }

        System.out.print("Новое имя (" + user.getFullName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            user.setFullName(name);
        }

        System.out.print("Новый паспорт (" + user.getPassport() + "): ");
        String passport = scanner.nextLine();
        if (!passport.isEmpty()) {
            user.setPassport(passport);
        }

        System.out.print("Новый телефон (" + user.getPhone() + "): ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty()) {
            user.setPhone(phone);
        }

        userDAO.update(user);
        System.out.println("Пользователь обновлен");
    }

    private static void deleteUser() throws SQLException {
        int id = getIntInput("Введите ID пользователя для удаления: ");

        System.out.print("Вы уверены? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            userDAO.delete(id);
            System.out.println("Пользователь удален");
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
            }
        }
    }

    private static String getOptionalInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static void pressEnterToContinue() {
        System.out.println("\nНажмите Enter для продолжения...");
        scanner.nextLine();
    }
}