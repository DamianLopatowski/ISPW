package org.example.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pagamento {
    private String clienteUsername;
    private double importo;
    private LocalDateTime dataPagamento;

    public Pagamento() {
        //riempimento
    }

    public Pagamento(String clienteUsername, double importo) {
        this.clienteUsername = clienteUsername;
        this.importo = importo;
        this.dataPagamento = LocalDateTime.now();
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
        return "Pagamento{" +
                "clienteUsername='" + clienteUsername + '\'' +
                ", importo=" + importo +
                ", dataPagamento=" + getDataPagamentoFormattata() +
                '}';
    }

}