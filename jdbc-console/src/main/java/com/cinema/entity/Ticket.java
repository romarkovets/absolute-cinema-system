package com.cinema.entity;

import java.time.LocalDateTime;

public class Ticket {
    private int ticketId;
    private int sessionId;
    private int userId;
    private int seatNumber;
    private LocalDateTime purchaseDate;

    public Ticket() {}

    public Ticket(int sessionId, int userId, int seatNumber) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.seatNumber = seatNumber;
        this.purchaseDate = LocalDateTime.now();
    }

    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }

    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }

    @Override
    public String toString() {
        return String.format("Билет ID: %d | Место: %d | %s",
                ticketId, seatNumber, purchaseDate.toLocalDate());
    }
}