package org.example.bean;

import org.example.model.Prodotto;

public class ProdottoBean {
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

    // 🔹 Costruttore vuoto
    public ProdottoBean() {}

    // 🔁 Conversione in model Prodotto
    public Prodotto toProdotto() {
        return new Prodotto.Builder()
                .id(id)
                .nome(nome)
                .quantita(quantita)
                .scaffale(scaffale)
                .codiceAbarre(codiceAbarre)
                .soglia(soglia)
                .prezzoAcquisto(prezzoAcquisto)
                .prezzoVendita(prezzoVendita)
                .categoria(categoria)
                .immagine(immagine)
                .build();
    }

    // ✅ Builder pattern per creare ProdottoBean
    public static class Builder {
        private final ProdottoBean bean;

        public Builder() {
            bean = new ProdottoBean();
        }

        public Builder id(int id) {
            bean.setId(id);
            return this;
        }

        public Builder nome(String nome) {
            bean.setNome(nome);
            return this;
        }

        public Builder quantita(int quantita) {
            bean.setQuantita(quantita);
            return this;
        }

        public Builder scaffale(String scaffale) {
            bean.setScaffale(scaffale);
            return this;
        }

        public Builder codiceAbarre(String codiceAbarre) {
            bean.setCodiceAbarre(codiceAbarre);
            return this;
        }

        public Builder soglia(int soglia) {
            bean.setSoglia(soglia);
            return this;
        }

        public Builder prezzoAcquisto(double prezzoAcquisto) {
            bean.setPrezzoAcquisto(prezzoAcquisto);
            return this;
        }

        public Builder prezzoVendita(double prezzoVendita) {
            bean.setPrezzoVendita(prezzoVendita);
            return this;
        }

        public Builder categoria(String categoria) {
            bean.setCategoria(categoria);
            return this;
        }

        public Builder immagine(byte[] immagine) {
            bean.setImmagine(immagine);
            return this;
        }

        public ProdottoBean build() {
            return bean;
        }
    }

    // Getter e Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }

    public String getScaffale() { return scaffale; }
    public void setScaffale(String scaffale) { this.scaffale = scaffale; }

    public String getCodiceAbarre() { return codiceAbarre; }
    public void setCodiceAbarre(String codiceAbarre) { this.codiceAbarre = codiceAbarre; }

    public int getSoglia() { return soglia; }
    public void setSoglia(int soglia) { this.soglia = soglia; }

    public double getPrezzoAcquisto() { return prezzoAcquisto; }
    public void setPrezzoAcquisto(double prezzoAcquisto) { this.prezzoAcquisto = prezzoAcquisto; }

    public double getPrezzoVendita() { return prezzoVendita; }
    public void setPrezzoVendita(double prezzoVendita) { this.prezzoVendita = prezzoVendita; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public byte[] getImmagine() { return immagine; }
    public void setImmagine(byte[] immagine) { this.immagine = immagine; }

    public Prodotto toModel() {
        return new Prodotto.Builder()
                .id(id)
                .nome(nome)
                .quantita(quantita)
                .scaffale(scaffale)
                .codiceAbarre(codiceAbarre)
                .soglia(soglia)
                .prezzoAcquisto(prezzoAcquisto)
                .prezzoVendita(prezzoVendita)
                .categoria(categoria)
                .immagine(immagine)
                .build();
    }
}
