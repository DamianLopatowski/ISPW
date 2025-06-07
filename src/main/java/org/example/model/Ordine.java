package org.example.model;

import java.time.LocalDateTime;
import java.util.Map;

public class Ordine {
    private int id; // solo per il DB
    private Cliente cliente;
    private LocalDateTime data;
    private Map<Prodotto, Integer> prodotti;
    private double totale;

    // 🔹 Costruttore usato normalmente per creare un ordine
    public Ordine(Cliente cliente, Map<Prodotto, Integer> prodotti, double totale) {
        this.cliente = cliente;
        this.prodotti = prodotti;
        this.data = LocalDateTime.now();
        this.totale = totale;
    }

    // 🔹 Costruttore vuoto usato dal DAO (per riempire dopo)
    public Ordine() {}

    // ✅ Descrizione leggibile dei prodotti per la tabella
    public String getDescrizioneProdotti() {
        if (prodotti == null || prodotti.isEmpty()) return "Nessun prodotto";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Prodotto, Integer> entry : prodotti.entrySet()) {
            sb.append(entry.getKey().getNome())
                    .append(" x")
                    .append(entry.getValue())
                    .append(", ");
        }
        return sb.substring(0, sb.length() - 2); // Rimuove l'ultima virgola
    }

    // ✅ Getter e Setter
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getData() {
        return data;
    }
    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Map<Prodotto, Integer> getProdotti() {
        return prodotti;
    }
    public void setProdotti(Map<Prodotto, Integer> prodotti) {
        this.prodotti = prodotti;
    }

    public double getTotale() {
        return totale;
    }
    public void setTotale(double totale) {
        this.totale = totale;
    }
}
