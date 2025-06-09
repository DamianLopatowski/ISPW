package org.example.view;

import javafx.beans.property.*;
import org.example.model.Pagamento;

import java.time.format.DateTimeFormatter;

public class PagamentoTableRow {
    private final StringProperty data;
    private final DoubleProperty importo;

    private final Pagamento pagamentoOriginale;

    public PagamentoTableRow(Pagamento pagamento) {
        this.pagamentoOriginale = pagamento;
        this.data = new SimpleStringProperty(
                pagamento.getDataPagamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
        this.importo = new SimpleDoubleProperty(pagamento.getImporto());
    }

    public StringProperty dataProperty() {
        return data;
    }

    public DoubleProperty importoProperty() {
        return importo;
    }

    public Pagamento getPagamentoOriginale() {
        return pagamentoOriginale;
    }
}
