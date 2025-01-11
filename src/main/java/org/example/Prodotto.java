package org.example;

public class Prodotto {
    private String nome;
    private String scaffale;
    private String codiceBarre;
    private int quantita;
    private int soglia;
    private double prezzoAcquisto;
    private double prezzoVendita;
    private byte[] immagine;

    public Prodotto(String nome, String scaffale, String codiceBarre, int quantita, int soglia, double prezzoAcquisto, double prezzoVendita, byte[] immagine) {
        this.nome = nome;
        this.scaffale = scaffale;
        this.codiceBarre = codiceBarre;
        this.quantita = quantita;
        this.soglia = soglia;
        this.prezzoAcquisto = prezzoAcquisto;
        this.prezzoVendita = prezzoVendita;
        this.immagine = immagine;
    }

    public String getNome() {
        return nome;
    }

    public String getScaffale() {
        return scaffale;
    }

    public String getCodiceBarre() {
        return codiceBarre;
    }

    public int getQuantita() {
        return quantita;
    }

    public int getSoglia() {
        return soglia;
    }

    public double getPrezzoAcquisto() {
        return prezzoAcquisto;
    }

    public double getPrezzoVendita() {
        return prezzoVendita;
    }

    public byte[] getImmagine() {
        return immagine;
    }
}
