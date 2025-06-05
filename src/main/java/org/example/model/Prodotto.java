package org.example.model;

public class Prodotto {
    private int id;
    private String nome;
    private int quantita;
    private String scaffale;
    private String codiceAbarre;
    private int soglia;
    private double prezzoAcquisto;
    private double prezzoVendita;
    private String categoria;
    private byte[] immagine;

    public Prodotto(int id, String nome, int quantita, String scaffale, String codiceAbarre, int soglia,
                    double prezzoAcquisto, double prezzoVendita, String categoria, byte[] immagine) {
        this.id = id;
        this.nome = nome;
        this.quantita = quantita;
        this.scaffale = scaffale;
        this.codiceAbarre = codiceAbarre;
        this.soglia = soglia;
        this.prezzoAcquisto = prezzoAcquisto;
        this.prezzoVendita = prezzoVendita;
        this.categoria = categoria;
        this.immagine = immagine;
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public int getQuantita() { return quantita; }
    public String getScaffale() { return scaffale; }
    public String getCodiceAbarre() { return codiceAbarre; }
    public int getSoglia() { return soglia; }
    public double getPrezzoAcquisto() { return prezzoAcquisto; }
    public double getPrezzoVendita() { return prezzoVendita; }
    public String getCategoria() { return categoria; }
    public byte[] getImmagine() { return immagine; }

    // Setter per quantità
    public void setQuantita(int nuova) {
        this.quantita = nuova;
    }
}
