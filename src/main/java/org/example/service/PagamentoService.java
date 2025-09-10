package org.example.service;
import org.example.dao.PagamentoDAO;
import org.example.model.Pagamento;

import java.util.List;

public class PagamentoService {
    private final PagamentoDAO pagamentoDAO;
    public PagamentoService(PagamentoDAO pagamentoDAO) {
        this.pagamentoDAO = pagamentoDAO;
    }

    public void registraPagamento(Pagamento pagamento) {
        pagamentoDAO.registraPagamento(pagamento);
    }

    public List<Pagamento> getPagamentiPerCliente(String username) {
        return pagamentoDAO.getPagamentiPerCliente(username);
    }
}