package com.cinema.dao;

import com.cinema.entity.MovieActor;
import com.cinema.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieActorDAO {

    public void create(MovieActor movieActor) throws SQLException {
        String sql = "INSERT INTO movie_actor (movie_id, actor_id, character_name) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieActor.getMovieId());
            stmt.setInt(2, movieActor.getActorId());
            stmt.setString(3, movieActor.getCharacterName());
            stmt.executeUpdate();
        }
    }

    public List<MovieActor> findByMovieId(int movieId) throws SQLException {
        List<MovieActor> movieActors = new ArrayList<>();
        String sql = "SELECT * FROM movie_actor WHERE movie_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MovieActor ma = new MovieActor();
                ma.setMovieId(rs.getInt("movie_id"));
                ma.setActorId(rs.getInt("actor_id"));
                ma.setCharacterName(rs.getString("character_name"));
                movieActors.add(ma);
            }
        }
        return movieActors;
    }

    public List<MovieActor> findByActorId(int actorId) throws SQLException {
        List<MovieActor> movieActors = new ArrayList<>();
        String sql = "SELECT * FROM movie_actor WHERE actor_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, actorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MovieActor ma = new MovieActor();
                ma.setMovieId(rs.getInt("movie_id"));
                ma.setActorId(rs.getInt("actor_id"));
                ma.setCharacterName(rs.getString("character_name"));
                movieActors.add(ma);
            }
        }
        return movieActors;
    }

    public void delete(int movieId, int actorId) throws SQLException {
        String sql = "DELETE FROM movie_actor WHERE movie_id = ? AND actor_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            stmt.setInt(2, actorId);
            stmt.executeUpdate();
        }
    }

    public void deleteByMovieId(int movieId) throws SQLException {
        String sql = "DELETE FROM movie_actor WHERE movie_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            stmt.executeUpdate();
        }
    }
}