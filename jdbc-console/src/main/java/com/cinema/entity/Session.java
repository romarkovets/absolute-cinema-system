package com.cinema.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Session {
    private int sessionId;
    private int movieId;
    private int hallId;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private double price;

    public Session() {}

    public Session(int movieId, int hallId, LocalDate sessionDate, LocalTime startTime, double price) {
        this.movieId = movieId;
        this.hallId = hallId;
        this.sessionDate = sessionDate;
        this.startTime = startTime;
        this.price = price;
    }

    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public int getHallId() { return hallId; }
    public void setHallId(int hallId) { this.hallId = hallId; }

    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s %s | %d руб",
                sessionId, sessionDate, startTime, (int)price);
    }
}