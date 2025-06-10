package org.example.service;

import javafx.beans.property.*;

public class ProdottoRow {
    private final IntegerProperty id;
    private final StringProperty nome;
    private final IntegerProperty quantita;
    private final IntegerProperty soglia;
    private final BooleanProperty ordinato;
    public ProdottoRow(int id, String nome, int quantita, int soglia, boolean ordinato) {
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.quantita = new SimpleIntegerProperty(quantita);
        this.soglia = new SimpleIntegerProperty(soglia);
        this.ordinato = new SimpleBooleanProperty(ordinato);
    }

    // Costruttore secondario per compatibilità con codice esistente
    public ProdottoRow(int id, String nome, int quantita, int soglia) {
        this(id, nome, quantita, soglia, false);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getNome() {
        return nome.get();
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public int getQuantita() {
        return quantita.get();
    }

    public IntegerProperty quantitaProperty() {
        return quantita;
    }

    public int getSoglia() {
        return soglia.get();
    }

    public IntegerProperty sogliaProperty() {
        return soglia;
    }

    public boolean isOrdinato() {
        return ordinato.get();
    }

    public BooleanProperty ordinatoProperty() {
        return ordinato;
    }

    public void setOrdinato(boolean value) {
        this.ordinato.set(value);
    }
}
