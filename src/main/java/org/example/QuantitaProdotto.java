package org.example;

public class QuantitaProdotto {
    private int quantita;
    private int soglia;

    // Costruttore
    public QuantitaProdotto(int quantita, int soglia) {
        this.quantita = quantita;
        this.soglia = soglia;
    }

    public int getQuantita() {
        return quantita;
    }

    public int getSoglia() {
        return soglia;
    }
}