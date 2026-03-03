package com.cinema.dao;

import com.cinema.entity.Session;
import com.cinema.util.DatabaseUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO {

    public void create(Session session) throws SQLException {
        String sql = "INSERT INTO \"session\" (movie_id, hall_id, session_date, start_time, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, session.getMovieId());
            stmt.setInt(2, session.getHallId());
            stmt.setDate(3, Date.valueOf(session.getSessionDate()));
            stmt.setTime(4, Time.valueOf(session.getStartTime()));
            stmt.setDouble(5, session.getPrice());
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                session.setSessionId(generatedKeys.getInt(1));
            }
        }
    }

    public List<Session> findAll() throws SQLException {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM \"session\" ORDER BY session_date, start_time";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Session session = new Session();
                session.setSessionId(rs.getInt("session_id"));
                session.setMovieId(rs.getInt("movie_id"));
                session.setHallId(rs.getInt("hall_id"));
                session.setSessionDate(rs.getDate("session_date").toLocalDate());
                session.setStartTime(rs.getTime("start_time").toLocalTime());
                session.setPrice(rs.getDouble("price"));
                sessions.add(session);
            }
        }
        return sessions;
    }

    public Session findById(int id) throws SQLException {
        String sql = "SELECT * FROM \"session\" WHERE session_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Session session = new Session();
                session.setSessionId(rs.getInt("session_id"));
                session.setMovieId(rs.getInt("movie_id"));
                session.setHallId(rs.getInt("hall_id"));
                session.setSessionDate(rs.getDate("session_date").toLocalDate());
                session.setStartTime(rs.getTime("start_time").toLocalTime());
                session.setPrice(rs.getDouble("price"));
                return session;
            }
        }
        return null;
    }

    public List<Session> findByDate(LocalDate date) throws SQLException {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM \"session\" WHERE session_date = ? ORDER BY start_time";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Session session = new Session();
                session.setSessionId(rs.getInt("session_id"));
                session.setMovieId(rs.getInt("movie_id"));
                session.setHallId(rs.getInt("hall_id"));
                session.setSessionDate(rs.getDate("session_date").toLocalDate());
                session.setStartTime(rs.getTime("start_time").toLocalTime());
                session.setPrice(rs.getDouble("price"));
                sessions.add(session);
            }
        }
        return sessions;
    }

    public void update(Session session) throws SQLException {
        String sql = "UPDATE \"session\" SET movie_id = ?, hall_id = ?, session_date = ?, start_time = ?, price = ? WHERE session_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, session.getMovieId());
            stmt.setInt(2, session.getHallId());
            stmt.setDate(3, Date.valueOf(session.getSessionDate()));
            stmt.setTime(4, Time.valueOf(session.getStartTime()));
            stmt.setDouble(5, session.getPrice());
            stmt.setInt(6, session.getSessionId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM \"session\" WHERE session_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}