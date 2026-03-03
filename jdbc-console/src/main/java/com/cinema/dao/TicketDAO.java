package com.cinema.dao;

import com.cinema.entity.Ticket;
import com.cinema.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    public void create(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO ticket (session_id, user_id, seat_number, purchase_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, ticket.getSessionId());
            stmt.setInt(2, ticket.getUserId());
            stmt.setInt(3, ticket.getSeatNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(ticket.getPurchaseDate()));
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                ticket.setTicketId(generatedKeys.getInt(1));
            }
        }
    }

    public List<Ticket> findAll() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket ORDER BY purchase_date DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setSessionId(rs.getInt("session_id"));
                ticket.setUserId(rs.getInt("user_id"));
                ticket.setSeatNumber(rs.getInt("seat_number"));
                ticket.setPurchaseDate(rs.getTimestamp("purchase_date").toLocalDateTime());
                tickets.add(ticket);
            }
        }
        return tickets;
    }

    public Ticket findById(int id) throws SQLException {
        String sql = "SELECT * FROM ticket WHERE ticket_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setSessionId(rs.getInt("session_id"));
                ticket.setUserId(rs.getInt("user_id"));
                ticket.setSeatNumber(rs.getInt("seat_number"));
                ticket.setPurchaseDate(rs.getTimestamp("purchase_date").toLocalDateTime());
                return ticket;
            }
        }
        return null;
    }

    public List<Ticket> findBySessionId(int sessionId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE session_id = ? ORDER BY seat_number";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sessionId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setSessionId(rs.getInt("session_id"));
                ticket.setUserId(rs.getInt("user_id"));
                ticket.setSeatNumber(rs.getInt("seat_number"));
                ticket.setPurchaseDate(rs.getTimestamp("purchase_date").toLocalDateTime());
                tickets.add(ticket);
            }
        }
        return tickets;
    }

    public List<Ticket> findByUserId(int userId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE user_id = ? ORDER BY purchase_date DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setSessionId(rs.getInt("session_id"));
                ticket.setUserId(rs.getInt("user_id"));
                ticket.setSeatNumber(rs.getInt("seat_number"));
                ticket.setPurchaseDate(rs.getTimestamp("purchase_date").toLocalDateTime());
                tickets.add(ticket);
            }
        }
        return tickets;
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM ticket WHERE ticket_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public int countBySessionId(int sessionId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ticket WHERE session_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sessionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}