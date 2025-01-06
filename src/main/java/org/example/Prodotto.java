package org.example;

import javafx.beans.property.*;

public class Prodotto {
    private StringProperty nome;
    private IntegerProperty quantita;
    private StringProperty scaffale;
    private StringProperty codiceBarre;
    private IntegerProperty soglia;
    private DoubleProperty prezzoAcquisto;
    private DoubleProperty prezzoVendita;

    // Costruttore
    public Prodotto(String nome, int quantita, String scaffale, String codiceBarre, double prezzoAcquisto, double prezzoVendita) {
        this.nome = new SimpleStringProperty(nome);
        this.quantita = new SimpleIntegerProperty(quantita);
        this.scaffale = new SimpleStringProperty(scaffale);
        this.codiceBarre = new SimpleStringProperty(codiceBarre);
        this.soglia = new SimpleIntegerProperty(0); // Soglia iniziale
        this.prezzoAcquisto = new SimpleDoubleProperty(prezzoAcquisto);
        this.prezzoVendita = new SimpleDoubleProperty(prezzoVendita);
    }

    // Getter e Setter per le proprietà
    public StringProperty nomeProperty() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getNome() {
        return nome.get();
    }

    public IntegerProperty quantitaProperty() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita.set(quantita);
    }

    public int getQuantita() {
        return quantita.get();
    }

    public StringProperty scaffaleProperty() {
        return scaffale;
    }

    public void setScaffale(String scaffale) {
        this.scaffale.set(scaffale);
    }

    public String getScaffale() {
        return scaffale.get();
    }

    public StringProperty codiceBarreProperty() {
        return codiceBarre;
    }

    public void setCodiceBarre(String codiceBarre) {
        this.codiceBarre.set(codiceBarre);
    }

    public String getCodiceBarre() {
        return codiceBarre.get();
    }

    public IntegerProperty sogliaProperty() {
        return soglia;
    }

    public void setSoglia(int soglia) {
        this.soglia.set(soglia);
    }

    public int getSoglia() {
        return soglia.get();
    }

    public DoubleProperty prezzoAcquistoProperty() {
        return prezzoAcquisto;
    }

    public void setPrezzoAcquisto(double prezzoAcquisto) {
        this.prezzoAcquisto.set(prezzoAcquisto);
    }

    public double getPrezzoAcquisto() {
        return prezzoAcquisto.get();
    }

    public DoubleProperty prezzoVenditaProperty() {
        return prezzoVendita;
    }

    public void setPrezzoVendita(double prezzoVendita) {
        this.prezzoVendita.set(prezzoVendita);
    }

    public double getPrezzoVendita() {
        return prezzoVendita.get();
    }

    @Override
    public String toString() {
        return nome.get() + ", Quantità: " + quantita.get() + ", Prezzo Acquisto: " + prezzoAcquisto.get();
    }
}
