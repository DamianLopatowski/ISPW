package org.example.bean;

import org.example.model.Pagamento;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PagamentoBean {
    private String clienteUsername;
    private double importo;
    private LocalDateTime dataPagamento;

    public PagamentoBean() {
        this.dataPagamento = LocalDateTime.now();
    }

    public Pagamento toPagamento() {
        Pagamento pagamento = new Pagamento();
        pagamento.setClienteUsername(clienteUsername);
        pagamento.setImporto(importo);
        pagamento.setDataPagamento(dataPagamento);
        return pagamento;
    }

    public String getClienteUsername() {
        return clienteUsername;
    }

    public void setClienteUsername(String clienteUsername) {
        this.clienteUsername = clienteUsername;
    }

    public double getImporto() {
        return importo;
    }

    public void setImporto(double importo) {
        this.importo = importo;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getDataPagamentoFormattata() {
        if (dataPagamento == null) return "N/D";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dataPagamento.format(formatter);
    }

    @Override
    public String toString() {
        return "PagamentoBean{" +
                "clienteUsername='" + clienteUsername + '\'' +
                ", importo=" + importo +
                ", dataPagamento=" + getDataPagamentoFormattata() +
                '}';
    }
}
