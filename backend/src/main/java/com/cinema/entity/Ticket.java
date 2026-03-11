package com.cinema.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketId;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @PrePersist
    protected void onCreate() {
        purchaseDate = LocalDateTime.now();
    }

    public void setSeatNumber(Integer seatNumber) {
        if (seatNumber <= 0) {
            throw new IllegalArgumentException("Seat number must be positive");
        }
        this.seatNumber = seatNumber;
    }
}