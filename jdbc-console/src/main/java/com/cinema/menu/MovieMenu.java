package com.cinema.menu;

import com.cinema.dao.*;
import com.cinema.entity.*;
import com.cinema.Main;

import java.sql.SQLException;
import java.util.List;

public class MovieMenu {

    private final MovieDAO movieDAO = new MovieDAO();
    private final ActorDAO actorDAO = new ActorDAO();
    private final MovieActorDAO movieActorDAO = new MovieActorDAO();

    public void showMenu() {
        while (true) {
            printMenu();
            int choice = Main.getIntInput("Выберите действие: ");

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

            Main.pressEnterToContinue();
        }
    }

    private void printMenu() {
        System.out.println("\n--- УПРАВЛЕНИЕ ФИЛЬМАМИ ---");
        System.out.println("1. Показать все фильмы");
        System.out.println("2. Найти фильм по ID");
        System.out.println("3. Добавить фильм");
        System.out.println("4. Обновить фильм");
        System.out.println("5. Удалить фильм");
        System.out.println("0. Назад");
    }

    private void showAllMovies() throws SQLException {
        List<Movie> movies = movieDAO.findAll();
        System.out.println("\n--- СПИСОК ФИЛЬМОВ ---");
        if (movies.isEmpty()) {
            System.out.println("Фильмов нет");
        } else {
            for (Movie m : movies) {
                System.out.println(m);
                showMovieActors(m.getMovieId());
            }
        }
    }

    private void showMovieActors(int movieId) throws SQLException {
        List<MovieActor> movieActors = movieActorDAO.findByMovieId(movieId);
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

    private void findMovieById() throws SQLException {
        int id = Main.getIntInput("Введите ID фильма: ");
        Movie movie = movieDAO.findById(id);
        if (movie == null) {
            System.out.println("Фильм не найден");
        } else {
            System.out.println("\n" + movie);
            System.out.println("Описание: " + movie.getDescription());
            showMovieActors(id);
        }
    }

    private void addMovie() throws SQLException {
        System.out.println("\n--- ДОБАВЛЕНИЕ ФИЛЬМА ---");
        String title = Main.getStringInput("Название: ");
        String description = Main.getStringInput("Описание: ");
        int duration = Main.getIntInput("Длительность (мин): ");
        int year = Main.getIntInput("Год выпуска: ");

        Movie movie = new Movie(title, description, duration, year);
        movieDAO.create(movie);
        System.out.println("Фильм добавлен! ID: " + movie.getMovieId());
    }

    private void updateMovie() throws SQLException {
        int id = Main.getIntInput("Введите ID фильма для обновления: ");
        Movie movie = movieDAO.findById(id);

        if (movie == null) {
            System.out.println("Фильм не найден");
            return;
        }

        System.out.println("Текущие данные: " + movie);
        System.out.println("Оставьте поле пустым, чтобы не менять");

        String title = Main.getOptionalInput("Новое название (" + movie.getTitle() + "): ");
        if (!title.isEmpty()) movie.setTitle(title);

        String desc = Main.getOptionalInput("Новое описание: ");
        if (!desc.isEmpty()) movie.setDescription(desc);

        String input = Main.getOptionalInput("Новая длительность (" + movie.getDurationMinutes() + "): ");
        if (!input.isEmpty()) movie.setDurationMinutes(Integer.parseInt(input));

        input = Main.getOptionalInput("Новый год (" + movie.getReleaseYear() + "): ");
        if (!input.isEmpty()) movie.setReleaseYear(Integer.parseInt(input));

        movieDAO.update(movie);
        System.out.println("Фильм обновлен");
    }

    private void deleteMovie() throws SQLException {
        int id = Main.getIntInput("Введите ID фильма для удаления: ");

        System.out.print("Вы уверены? (y/n): ");
        String confirm = Main.getStringInput("");

        if (confirm.equalsIgnoreCase("y")) {
            movieActorDAO.deleteByMovieId(id);
            movieDAO.delete(id);
            System.out.println("Фильм удален");
        }
    }
}