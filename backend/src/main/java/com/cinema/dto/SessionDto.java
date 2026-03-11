package com.cinema.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SessionDto {
    private Integer sessionId;
    private MovieDto movie;
    private HallDto hall;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private BigDecimal price;
    private Integer availableSeats;
}