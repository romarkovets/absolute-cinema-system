package com.cinema.entity;

public class Hall {
    private int hallId;
    private int hallNumber;
    private int hallType;

    public Hall() {}

    public Hall(int hallNumber, int hallType) {
        this.hallNumber = hallNumber;
        this.hallType = hallType;
    }

    public int getHallId() { return hallId; }
    public void setHallId(int hallId) { this.hallId = hallId; }

    public int getHallNumber() { return hallNumber; }
    public void setHallNumber(int hallNumber) { this.hallNumber = hallNumber; }

    public int getHallType() { return hallType; }
    public void setHallType(int hallType) { this.hallType = hallType; }

    @Override
    public String toString() {
        String type = hallType == 1 ? "Обычный" : hallType == 2 ? "VIP" : "IMAX";
        return String.format("Зал %d | %s", hallNumber, type);
    }
}