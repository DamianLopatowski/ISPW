package org.example.model;

public class Cliente {
    private String username;
    private String nome;
    private String cognome;
    private String password;

    public Cliente(String username, String nome, String cognome, String password) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getPassword() { return password; }
}
