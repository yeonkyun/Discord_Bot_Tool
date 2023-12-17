package com.Discord_Bot_Tool.db;

import java.time.LocalDate;

public class Users {
    private String username;
    private String email;
    private long user_id;
    private LocalDate authordate;

    public Users() {
    }

    public Users(String username, String email, long user_id) {
        this.username = username;
        this.email = email;
        this.user_id = user_id;
    }

    public Users(String username, String email, long user_id, LocalDate authordate) {
        this.username = username;
        this.email = email;
        this.user_id = user_id;
        this.authordate = authordate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public LocalDate getAuthordate() {
        return authordate;
    }

    public void setAuthordate(LocalDate authordate) {
        this.authordate = authordate;
    }

    @Override
    public String toString() {
        return username + " " + email + " " + user_id + " " + authordate;
    }
}
