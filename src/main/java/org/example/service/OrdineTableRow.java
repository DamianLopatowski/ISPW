package org.example.service;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.example.bean.ClienteBean;
import org.example.bean.OrdineBean;

public class OrdineTableRow {

    private final OrdineBean ordineBean;

    private final SimpleStringProperty cliente;
    private final SimpleStringProperty data;
    private final SimpleDoubleProperty importo;
    private final SimpleStringProperty prodotti;
    private final SimpleStringProperty stato;
    private final SimpleStringProperty indirizzo;
    private final SimpleStringProperty codiceSpedizione;

    public OrdineTableRow(OrdineBean ordine) {
        this.ordineBean = ordine;

        ClienteBean c = ordine.getCliente();

        String username = (c != null && c.getUsername() != null && !c.getUsername().isBlank())
                ? c.getUsername()
                : "N/D";

        String indirizzoCompleto = (c != null)
                ? String.format("%s %s, %s %s",
                nullToEmpty(c.getIndirizzo()),
                nullToEmpty(c.getCivico()),
                nullToEmpty(c.getCap()),
                nullToEmpty(c.getCitta())
        ).trim()
                : "Indirizzo non disponibile";

        this.cliente = new SimpleStringProperty(username);
        this.data = new SimpleStringProperty(ordine.getData().toString());
        this.importo = new SimpleDoubleProperty(ordine.getTotale());
        this.prodotti = new SimpleStringProperty(ordine.getDescrizioneProdotti());
        this.stato = new SimpleStringProperty(ordine.isSpedito() ? "Spedito" : "Non spedito");
        this.indirizzo = new SimpleStringProperty(indirizzoCompleto);
        this.codiceSpedizione = new SimpleStringProperty(ordine.getCodiceSpedizione());
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s.trim();
    }

    public SimpleStringProperty clienteProperty() {
        return cliente;
    }

    public SimpleStringProperty dataProperty() {
        return data;
    }

    public SimpleDoubleProperty importoProperty() {
        return importo;
    }

    public SimpleStringProperty prodottiProperty() {
        return prodotti;
    }

    public SimpleStringProperty statoProperty() {
        return stato;
    }

    public SimpleStringProperty indirizzoProperty() {
        return indirizzo;
    }

    public SimpleStringProperty codiceSpedizioneProperty() {
        return codiceSpedizione;
    }

    public String getClienteUsername() {
        return ordineBean.getCliente() != null ? ordineBean.getCliente().getUsername() : "N/D";
    }

    public double getTotaleOrdine() {
        return ordineBean.getTotale();
    }

    public String getCodiceSpedizione() {
        return codiceSpedizione.get();
    }

    public OrdineBean toOrdineBean() {
        return ordineBean;
    }
}
