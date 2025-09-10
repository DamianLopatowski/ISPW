package org.example.dao;
import org.example.model.Pagamento;
import java.util.List;

public interface PagamentoDAO {
    void registraPagamento(Pagamento pagamento);
    List<Pagamento> getPagamentiPerCliente(String username);
}
