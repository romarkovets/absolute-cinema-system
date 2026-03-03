package com.cinema.entity;

public class Movie {
    private int movieId;
    private String title;
    private String description;
    private int durationMinutes;
    private int releaseYear;

    public Movie() {}

    public Movie(String title, String description, int durationMinutes, int releaseYear) {
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.releaseYear = releaseYear;
    }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s (%d) | %d мин",
                movieId, title, releaseYear, durationMinutes);
    }
}