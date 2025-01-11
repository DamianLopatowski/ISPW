package org.example;

public class DettagliProdotto {
    private double prezzoAcquisto;
    private double prezzoVendita;

    // Costruttore
    public DettagliProdotto(double prezzoAcquisto, double prezzoVendita) {
        this.prezzoAcquisto = prezzoAcquisto;
        this.prezzoVendita = prezzoVendita;
    }

    public double getPrezzoAcquisto() {
        return prezzoAcquisto;
    }

    public double getPrezzoVendita() {
        return prezzoVendita;
    }
}
