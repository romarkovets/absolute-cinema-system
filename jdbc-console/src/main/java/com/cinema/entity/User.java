package com.cinema.entity;

public class User {
    private int userId;
    private String email;
    private String fullName;
    private String passport;
    private String phone;

    public User() {}

    public User(String email, String fullName, String passport, String phone) {
        this.email = email;
        this.fullName = fullName;
        this.passport = passport;
        this.phone = phone;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPassport() { return passport; }
    public void setPassport(String passport) { this.passport = passport; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s", userId, fullName, email);
    }
}