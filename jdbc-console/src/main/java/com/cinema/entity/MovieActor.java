package com.cinema.entity;

public class MovieActor {
    private int movieId;
    private int actorId;
    private String characterName;

    public MovieActor() {}

    public MovieActor(int movieId, int actorId, String characterName) {
        this.movieId = movieId;
        this.actorId = actorId;
        this.characterName = characterName;
    }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public int getActorId() { return actorId; }
    public void setActorId(int actorId) { this.actorId = actorId; }

    public String getCharacterName() { return characterName; }
    public void setCharacterName(String characterName) { this.characterName = characterName; }

    @Override
    public String toString() {
        return String.format("Movie ID: %d, Actor ID: %d, Role: %s",
                movieId, actorId, characterName);
    }
}