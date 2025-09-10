package org.example.model;
public class Gestore {
    private String username;
    private String password;

    public Gestore(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
