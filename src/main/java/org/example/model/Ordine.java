package org.example.model;

import java.time.LocalDateTime;
import java.util.Map;

public class Ordine {
    private int id; // solo per DB
    private Cliente cliente;
    private LocalDateTime data;
    private Map<Prodotto, Integer> prodotti;
    private double totale;

    // Costruttore
    public Ordine(Cliente cliente, Map<Prodotto, Integer> prodotti, double totale) {
        this.cliente = cliente;
        this.prodotti = prodotti;
        this.data = LocalDateTime.now();
        this.totale = totale;
    }

    // Getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public LocalDateTime getData() { return data; }
    public Map<Prodotto, Integer> getProdotti() { return prodotti; }
    public double getTotale() { return totale; }
}
