package org.example;
public class Prodotto { //ciao
    private String nome;
    private int quantita;
    private String scaffale;
    private String codiceBarre;
    private int soglia;
    private double prezzoAcquisto; // Nuovo attributo
    private double prezzoVendita;   // Nuovo attributo

    public Prodotto(String nome, int quantita, String scaffale, String codiceBarre, double prezzoAcquisto, double prezzoVendita) {
        this.nome = nome;
        this.quantita = quantita;
        this.scaffale = scaffale;
        this.codiceBarre = codiceBarre;
        this.soglia = 0; // Soglia di default
        this.prezzoAcquisto = prezzoAcquisto; // Imposta il prezzo di acquisto
        this.prezzoVendita = prezzoVendita;   // Imposta il prezzo di vendita
    }

    // Getter e Setter per tutti i campi inclusi i nuovi attributi
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getScaffale() {
        return scaffale;
    }

    public void setScaffale(String scaffale) {
        this.scaffale = scaffale;
    }

    public String getCodiceBarre() {
        return codiceBarre;
    }

    public void setCodiceBarre(String codiceBarre) {
        this.codiceBarre = codiceBarre;
    }

    public int getSoglia() {
        return soglia;
    }

    public void setSoglia(int soglia) {
        this.soglia = soglia;
    }

    public double getPrezzoAcquisto() {
        return prezzoAcquisto; // Aggiunto
    }

    public void setPrezzoAcquisto(double prezzoAcquisto) {
        this.prezzoAcquisto = prezzoAcquisto; // Aggiunto
    }

    public double getPrezzoVendita() {
        return prezzoVendita; // Aggiunto
    }

    public void setPrezzoVendita(double prezzoVendita) {
        this.prezzoVendita = prezzoVendita; // Aggiunto
    }
}

