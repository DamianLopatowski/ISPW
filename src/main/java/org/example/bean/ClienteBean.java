package org.example.bean;

import org.example.model.Cliente;

public class ClienteBean {
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

    public ClienteBean() {
        //riempimento
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPartitaIva() {
        return partitaIva;
    }
    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public String getIndirizzo() {
        return indirizzo;
    }
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCivico() {
        return civico;
    }
    public void setCivico(String civico) {
        this.civico = civico;
    }

    public String getCap() {
        return cap;
    }
    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCitta() {
        return citta;
    }
    public void setCitta(String citta) {
        this.citta = citta;
    }

    public Cliente toCliente() {
        return new Cliente.Builder()
                .username(username)
                .nome(nome)
                .cognome(cognome)
                .password(password)
                .email(email)
                .partitaIva(partitaIva)
                .indirizzo(indirizzo)
                .civico(civico)
                .cap(cap)
                .citta(citta)
                .build();
    }
}