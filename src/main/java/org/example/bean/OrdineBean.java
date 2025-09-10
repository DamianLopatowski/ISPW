package org.example.bean;

import org.example.model.Ordine;
import org.example.model.Prodotto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class OrdineBean {
    private int id;
    private ClienteBean cliente;
    private LocalDateTime data;
    private Map<ProdottoBean, Integer> prodotti;
    private double totale;
    private boolean spedito;
    private String codiceSpedizione;

    public OrdineBean() {
        this.data = LocalDateTime.now();
        this.prodotti = new HashMap<>();
    }

    public Ordine toOrdine() {
        Map<Prodotto, Integer> prodottiModel = new HashMap<>();
        for (Map.Entry<ProdottoBean, Integer> entry : prodotti.entrySet()) {
            prodottiModel.put(entry.getKey().toProdotto(), entry.getValue());
        }

        Ordine ordine = new Ordine();
        ordine.setId(id);
        ordine.setCliente(cliente != null ? cliente.toCliente() : null);
        ordine.setData(data);
        ordine.setProdotti(prodottiModel);
        ordine.setTotale(totale);

        ordine.setSpedito(spedito);
        ordine.setCodiceSpedizione(codiceSpedizione);

        return ordine;
    }

    public String getDescrizioneProdotti() {
        if (prodotti == null || prodotti.isEmpty()) return "Nessun prodotto";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<ProdottoBean, Integer> entry : prodotti.entrySet()) {
            sb.append(entry.getKey().getNome())
                    .append(" x")
                    .append(entry.getValue())
                    .append(", ");
        }
        return sb.substring(0, sb.length() - 2); // rimuove l'ultima virgola
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public ClienteBean getCliente() {
        return cliente;
    }
    public void setCliente(ClienteBean cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getData() {
        return data;
    }
    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Map<ProdottoBean, Integer> getProdotti() {
        return prodotti;
    }
    public void setProdotti(Map<ProdottoBean, Integer> prodotti) {
        this.prodotti = prodotti;
    }

    public double getTotale() {
        return totale;
    }
    public void setTotale(double totale) {
        this.totale = totale;
    }

    public boolean isSpedito() {
        return spedito;
    }
    public void setSpedito(boolean spedito) {
        this.spedito = spedito;
    }

    public String getCodiceSpedizione() {
        return codiceSpedizione;
    }
    public void setCodiceSpedizione(String codiceSpedizione) {
        this.codiceSpedizione = codiceSpedizione;
    }
}
