package com.cinema.menu;

import com.cinema.dao.*;
import com.cinema.entity.*;
import com.cinema.Main;

import java.sql.SQLException;
import java.util.List;

public class ActorMenu {

    private final ActorDAO actorDAO = new ActorDAO();
    private final MovieDAO movieDAO = new MovieDAO();
    private final MovieActorDAO movieActorDAO = new MovieActorDAO();

    public void showMenu() {
        while (true) {
            printMenu();
            int choice = Main.getIntInput("Выберите действие: ");

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

            Main.pressEnterToContinue();
        }
    }

    private void printMenu() {
        System.out.println("\n--- УПРАВЛЕНИЕ АКТЕРАМИ ---");
        System.out.println("1. Показать всех актеров");
        System.out.println("2. Найти актера по ID");
        System.out.println("3. Добавить актера");
        System.out.println("4. Обновить актера");
        System.out.println("5. Удалить актера");
        System.out.println("6. Связать актера с фильмом");
        System.out.println("0. Назад");
    }

    private void showAllActors() throws SQLException {
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

    private void findActorById() throws SQLException {
        int id = Main.getIntInput("Введите ID актера: ");
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

    private void addActor() throws SQLException {
        System.out.println("\n--- ДОБАВЛЕНИЕ АКТЕРА ---");
        String fullName = Main.getStringInput("Полное имя: ");
        String country = Main.getStringInput("Страна: ");

        Actor actor = new Actor(fullName, country);
        actorDAO.create(actor);
        System.out.println("Актер добавлен! ID: " + actor.getActorId());
    }

    private void updateActor() throws SQLException {
        int id = Main.getIntInput("Введите ID актера для обновления: ");
        Actor actor = actorDAO.findById(id);

        if (actor == null) {
            System.out.println("Актер не найден");
            return;
        }

        System.out.println("Текущие данные: " + actor);
        System.out.println("Оставьте поле пустым, чтобы не менять");

        String name = Main.getOptionalInput("Новое имя (" + actor.getFullName() + "): ");
        if (!name.isEmpty()) actor.setFullName(name);

        String country = Main.getOptionalInput("Новая страна (" + actor.getCountry() + "): ");
        if (!country.isEmpty()) actor.setCountry(country);

        actorDAO.update(actor);
        System.out.println("Актер обновлен");
    }

    private void deleteActor() throws SQLException {
        int id = Main.getIntInput("Введите ID актера для удаления: ");

        System.out.print("Вы уверены? (y/n): ");
        String confirm = Main.getStringInput("");

        if (confirm.equalsIgnoreCase("y")) {
            actorDAO.delete(id);
            System.out.println("Актер удален");
        }
    }

    private void linkActorToMovie() throws SQLException {
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
        for (Movie m : movies) System.out.println(m);
        int movieId = Main.getIntInput("Выберите ID фильма: ");

        System.out.println("\nАктеры:");
        for (Actor a : actors) System.out.println(a);
        int actorId = Main.getIntInput("Выберите ID актера: ");

        String characterName = Main.getStringInput("Имя персонажа: ");

        MovieActor movieActor = new MovieActor(movieId, actorId, characterName);
        movieActorDAO.create(movieActor);
        System.out.println("Связь создана!");
    }
}