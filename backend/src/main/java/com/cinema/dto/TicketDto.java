package com.cinema.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TicketDto {
    private Integer ticketId;
    private SessionDto session;
    private UserDto user;
    private Integer seatNumber;
    private LocalDateTime purchaseDate;
}