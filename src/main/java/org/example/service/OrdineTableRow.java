package org.example.service;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.example.bean.OrdineBean;
import org.example.model.Ordine;

public class OrdineTableRow {
    private final SimpleStringProperty data;
    private final SimpleDoubleProperty importo;
    private final SimpleStringProperty prodotti;

    public OrdineTableRow(OrdineBean ordine) {
        this.data = new SimpleStringProperty(ordine.getData().toString());
        this.importo = new SimpleDoubleProperty(ordine.getTotale());
        this.prodotti = new SimpleStringProperty(ordine.getDescrizioneProdotti());
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
}
