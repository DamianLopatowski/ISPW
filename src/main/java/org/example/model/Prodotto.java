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
    private boolean ordinato;

    private Prodotto(Builder builder) {
        this.id = builder.id;
        this.nome = builder.nome;
        this.quantita = builder.quantita;
        this.scaffale = builder.scaffale;
        this.codiceAbarre = builder.codiceAbarre;
        this.soglia = builder.soglia;
        this.prezzoAcquisto = builder.prezzoAcquisto;
        this.prezzoVendita = builder.prezzoVendita;
        this.categoria = builder.categoria;
        this.immagine = builder.immagine;
        this.ordinato = builder.ordinato;
    }


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

    public void setQuantita(int nuova) {
        this.quantita = nuova;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOrdinato() {
        return ordinato;
    }

    public void setOrdinato(boolean ordinato) {
        this.ordinato = ordinato;
    }

    public static class Builder {
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
        private boolean ordinato;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder quantita(int quantita) {
            this.quantita = quantita;
            return this;
        }

        public Builder scaffale(String scaffale) {
            this.scaffale = scaffale;
            return this;
        }

        public Builder codiceAbarre(String codiceAbarre) {
            this.codiceAbarre = codiceAbarre;
            return this;
        }

        public Builder soglia(int soglia) {
            this.soglia = soglia;
            return this;
        }

        public Builder prezzoAcquisto(double prezzoAcquisto) {
            this.prezzoAcquisto = prezzoAcquisto;
            return this;
        }

        public Builder prezzoVendita(double prezzoVendita) {
            this.prezzoVendita = prezzoVendita;
            return this;
        }

        public Builder categoria(String categoria) {
            this.categoria = categoria;
            return this;
        }

        public Builder immagine(byte[] immagine) {
            this.immagine = immagine;
            return this;
        }

        public Prodotto build() {
            return new Prodotto(this);
        }

        public void setId(int id) {
            this.id = id;
        }
        public Builder ordinato(boolean ordinato) {
            this.ordinato = ordinato;
            return this;
        }

    }

}