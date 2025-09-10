package org.example.model;
import java.time.LocalDateTime;
import java.util.Map;

public class Ordine {
    private int id;
    private Cliente cliente;
    private LocalDateTime data;
    private Map<Prodotto, Integer> prodotti;
    private double totale;
    private boolean spedito;
    private String codiceSpedizione;


    public Ordine(Cliente cliente, Map<Prodotto, Integer> prodotti, double totale) {
        this.cliente = cliente;
        this.prodotti = prodotti;
        this.data = LocalDateTime.now();
        this.totale = totale;
    }

    public Ordine() {}

    public String getDescrizioneProdotti() {
        if (prodotti == null || prodotti.isEmpty()) return "Nessun prodotto";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Prodotto, Integer> entry : prodotti.entrySet()) {
            sb.append(entry.getKey().getNome())
                    .append(" x")
                    .append(entry.getValue())
                    .append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

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
    public boolean isSpedito() { return spedito; }
    public void setSpedito(boolean spedito) { this.spedito = spedito; }

    public String getCodiceSpedizione() { return codiceSpedizione; }
    public void setCodiceSpedizione(String codiceSpedizione) { this.codiceSpedizione = codiceSpedizione; }

    public static Ordine creaDaBean(Cliente clienteBean, Map<org.example.bean.ProdottoBean, Integer> prodottiBean, double totale) {
        Ordine ordine = new Ordine();
        if (clienteBean != null) {
            ordine.cliente = clienteBean;
        }
        if (prodottiBean != null) {
            ordine.prodotti = new java.util.HashMap<>();
            for (Map.Entry<org.example.bean.ProdottoBean, Integer> entry : prodottiBean.entrySet()) {
                ordine.prodotti.put(entry.getKey().toModel(), entry.getValue());
            }
        }
        ordine.data = LocalDateTime.now();
        ordine.totale = totale;

        return ordine;
    }

}
