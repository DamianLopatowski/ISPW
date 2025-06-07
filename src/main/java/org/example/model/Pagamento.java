package org.example.model;

import java.time.LocalDateTime;

public class Pagamento {
    private String clienteUsername;
    private double importo;
    private LocalDateTime dataPagamento;

    public Pagamento() {}

    public Pagamento(String clienteUsername, double importo) {
        this.clienteUsername = clienteUsername;
        this.importo = importo;
        this.dataPagamento = LocalDateTime.now();
    }

    public String getClienteUsername() { return clienteUsername; }
    public void setClienteUsername(String clienteUsername) { this.clienteUsername = clienteUsername; }

    public double getImporto() { return importo; }
    public void setImporto(double importo) { this.importo = importo; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
}