package org.example.model;

public class Cliente {
    private String username;
    private String nome;
    private String cognome;
    private String password;
    private String email;

    public Cliente(String username, String nome, String cognome, String password, String email) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }
    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
}