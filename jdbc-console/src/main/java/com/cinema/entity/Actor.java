package com.cinema.entity;

public class Actor {
    private int actorId;
    private String fullName;
    private String country;

    public Actor() {}

    public Actor(String fullName, String country) {
        this.fullName = fullName;
        this.country = country;
    }

    public int getActorId() { return actorId; }
    public void setActorId(int actorId) { this.actorId = actorId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s", actorId, fullName, country);
    }
}