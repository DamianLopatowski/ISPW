package org.example.model;

public class Cliente {
    private String username;
    private String nome;
    private String cognome;
    private String password;
    private String email;
    private String partitaIva;
    private String indirizzo;
    private String civico;
    private String cap;
    private String citta;

    public Cliente(String username, String nome, String cognome, String password, String email, String partitaIva, String indirizzo, String civico, String cap, String citta) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
        this.email = email;
        this.partitaIva = partitaIva;
        this.indirizzo = indirizzo;
        this.civico = civico;
        this.cap = cap;
        this.citta = citta;
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
    public String getPartitaIva() {
        return partitaIva;
    }
    public String getIndirizzo() {
        return indirizzo;
    }
    public String getCivico() {
        return civico;
    }
    public String getCap() {
        return cap;
    }
    public String getCitta() {
        return citta;
    }
}
