package com.cinema.dao;

import com.cinema.entity.Actor;
import com.cinema.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorDAO {

    public void create(Actor actor) throws SQLException {
        String sql = "INSERT INTO actor (full_name, country) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, actor.getFullName());
            stmt.setString(2, actor.getCountry());
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                actor.setActorId(generatedKeys.getInt(1));
            }
        }
    }

    public List<Actor> findAll() throws SQLException {
        List<Actor> actors = new ArrayList<>();
        String sql = "SELECT * FROM actor ORDER BY full_name";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Actor actor = new Actor();
                actor.setActorId(rs.getInt("actor_id"));
                actor.setFullName(rs.getString("full_name"));
                actor.setCountry(rs.getString("country"));
                actors.add(actor);
            }
        }
        return actors;
    }

    public Actor findById(int id) throws SQLException {
        String sql = "SELECT * FROM actor WHERE actor_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Actor actor = new Actor();
                actor.setActorId(rs.getInt("actor_id"));
                actor.setFullName(rs.getString("full_name"));
                actor.setCountry(rs.getString("country"));
                return actor;
            }
        }
        return null;
    }

    public void update(Actor actor) throws SQLException {
        String sql = "UPDATE actor SET full_name = ?, country = ? WHERE actor_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, actor.getFullName());
            stmt.setString(2, actor.getCountry());
            stmt.setInt(3, actor.getActorId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM actor WHERE actor_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}