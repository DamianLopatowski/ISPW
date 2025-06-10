package org.example.service;

import javafx.beans.property.*;

public class ClienteRiepilogoRow {
    private final StringProperty username;
    private final StringProperty nome;
    private final DoubleProperty totaleOrdini;
    private final DoubleProperty totalePagato;
    private final DoubleProperty residuo;

    public ClienteRiepilogoRow(String username, String nome, double totaleOrdini, double totalePagato) {
        this.username = new SimpleStringProperty(username);
        this.nome = new SimpleStringProperty(nome);
        this.totaleOrdini = new SimpleDoubleProperty(totaleOrdini);
        this.totalePagato = new SimpleDoubleProperty(totalePagato);
        this.residuo = new SimpleDoubleProperty(totaleOrdini - totalePagato);
    }

    // Property per JavaFX binding
    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public DoubleProperty totaleOrdiniProperty() {
        return totaleOrdini;
    }

    public DoubleProperty totalePagatoProperty() {
        return totalePagato;
    }

    public DoubleProperty residuoProperty() {
        return residuo;
    }

    // Getter classici
    public String getUsername() {
        return username.get();
    }

    public String getNome() {
        return nome.get();
    }

    public double getTotaleOrdini() {
        return totaleOrdini.get();
    }

    public double getTotalePagato() {
        return totalePagato.get();
    }

    public double getResiduo() {
        return residuo.get();
    }
}