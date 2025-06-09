package org.example.service;

import javafx.beans.property.*;
import org.example.bean.PagamentoBean;

import java.time.format.DateTimeFormatter;

public class PagamentoTableRow {
    private final StringProperty data;
    private final DoubleProperty importo;

    private final PagamentoBean pagamentoOriginale;

    public PagamentoTableRow(PagamentoBean pagamento) {
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

    public PagamentoBean getPagamentoOriginale() {
        return pagamentoOriginale;
    }
}
