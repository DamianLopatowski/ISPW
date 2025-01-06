package org.example;

public class ProdottoUpdateRequest {
    private String nome;
    private String codiceBarre;
    private int quantita;
    private boolean prezzoAcquistoInserito;
    private double prezzoAcquisto;
    private boolean prezzoVenditaInserito;
    private double prezzoVendita;

    public ProdottoUpdateRequest(String nome, String codiceBarre, int quantita, boolean prezzoAcquistoInserito, double prezzoAcquisto, boolean prezzoVenditaInserito, double prezzoVendita) {
        this.nome = nome;
        this.codiceBarre = codiceBarre;
        this.quantita = quantita;
        this.prezzoAcquistoInserito = prezzoAcquistoInserito;
        this.prezzoAcquisto = prezzoAcquisto;
        this.prezzoVenditaInserito = prezzoVenditaInserito;
        this.prezzoVendita = prezzoVendita;
    }

    // Getter e Setter per tutti i campi
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodiceBarre() {
        return codiceBarre;
    }

    public void setCodiceBarre(String codiceBarre) {
        this.codiceBarre = codiceBarre;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public boolean isPrezzoAcquistoInserito() {
        return prezzoAcquistoInserito;
    }

    public void setPrezzoAcquistoInserito(boolean prezzoAcquistoInserito) {
        this.prezzoAcquistoInserito = prezzoAcquistoInserito;
    }

    public double getPrezzoAcquisto() {
        return prezzoAcquisto;
    }

    public void setPrezzoAcquisto(double prezzoAcquisto) {
        this.prezzoAcquisto = prezzoAcquisto;
    }

    public boolean isPrezzoVenditaInserito() {
        return prezzoVenditaInserito;
    }

    public void setPrezzoVenditaInserito(boolean prezzoVenditaInserito) {
        this.prezzoVenditaInserito = prezzoVenditaInserito;
    }

    public double getPrezzoVendita() {
        return prezzoVendita;
    }

    public void setPrezzoVendita(double prezzoVendita) {
        this.prezzoVendita = prezzoVendita;
    }
}
