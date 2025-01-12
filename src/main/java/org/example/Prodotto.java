package org.example;

public class Prodotto {
    private String nome;
    private String scaffale;
    private String codiceBarre;
    private DettagliProdotto dettagliProdotto;
    private QuantitaProdotto quantitaProdotto;
    private byte[] immagine;

    // Costruttore
    public Prodotto(String nome, String scaffale, String codiceBarre, DettagliProdotto dettagliProdotto, QuantitaProdotto quantitaProdotto, byte[] immagine) {
        this.nome = nome;
        this.scaffale = scaffale;
        this.codiceBarre = codiceBarre;
        this.dettagliProdotto = dettagliProdotto;
        this.quantitaProdotto = quantitaProdotto;
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

    public DettagliProdotto getDettagliProdotto() {
        return dettagliProdotto;
    }

    public QuantitaProdotto getQuantitaProdotto() {
        return quantitaProdotto;
    }

    public byte[] getImmagine() {
        return immagine;
    }
}