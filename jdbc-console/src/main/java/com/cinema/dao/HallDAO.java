package com.cinema.dao;

import com.cinema.entity.Hall;
import com.cinema.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HallDAO {

    public void create(Hall hall) throws SQLException {
        String sql = "INSERT INTO hall (hall_number, hall_type) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, hall.getHallNumber());
            stmt.setInt(2, hall.getHallType());
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                hall.setHallId(generatedKeys.getInt(1));
            }
        }
    }

    public List<Hall> findAll() throws SQLException {
        List<Hall> halls = new ArrayList<>();
        String sql = "SELECT * FROM hall ORDER BY hall_number";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Hall hall = new Hall();
                hall.setHallId(rs.getInt("hall_id"));
                hall.setHallNumber(rs.getInt("hall_number"));
                hall.setHallType(rs.getInt("hall_type"));
                halls.add(hall);
            }
        }
        return halls;
    }

    public Hall findById(int id) throws SQLException {
        String sql = "SELECT * FROM hall WHERE hall_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Hall hall = new Hall();
                hall.setHallId(rs.getInt("hall_id"));
                hall.setHallNumber(rs.getInt("hall_number"));
                hall.setHallType(rs.getInt("hall_type"));
                return hall;
            }
        }
        return null;
    }

    public void update(Hall hall) throws SQLException {
        String sql = "UPDATE hall SET hall_number = ?, hall_type = ? WHERE hall_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hall.getHallNumber());
            stmt.setInt(2, hall.getHallType());
            stmt.setInt(3, hall.getHallId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM hall WHERE hall_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}