package org.example.facade;

import org.example.bean.ClienteBean;
import org.example.dao.PagamentoDAO;
import org.example.model.Cliente;
import org.example.model.Prodotto;
import org.example.service.EmailService;

import java.util.Map;
import java.util.logging.Logger;

public class ClienteFacade {

    public ClienteFacade(PagamentoDAO pagamentoDAO) {
    }
    public void inviaConfermaPagamento(ClienteBean cliente, double importoBonificato, double totalePagato, double residuo) {
        if (cliente == null || cliente.getEmail() == null || cliente.getNome() == null) {
            Logger.getLogger(ClienteFacade.class.getName()).warning("Cliente o dati mancanti, impossibile inviare email di conferma pagamento.");
            return;
        }

        EmailService.sendPaymentConfirmationEmail(
                cliente.getEmail(),
                cliente.getNome(),
                importoBonificato,
                totalePagato,
                residuo
        );
    }

    public void inviaEmailConfermaRegistrazione(String email, String username) {
        EmailService.sendConfirmationEmail(email, username);
    }

    public void inviaEmailRiepilogoOrdine(Cliente cliente, Map<Prodotto, Integer> carrello) {
        EmailService.sendOrderSummaryEmail(
                cliente.getEmail(),
                cliente.getUsername(),
                carrello
        );
    }

    public void inviaConfermaSpedizione(ClienteBean cliente, String codiceSpedizione) {
        if (cliente == null || cliente.getEmail() == null || cliente.getNome() == null) {
            Logger.getLogger(ClienteFacade.class.getName()).warning("Cliente o dati mancanti, impossibile inviare email di spedizione.");
            return;
        }

        EmailService.sendShippingConfirmationEmail(
                cliente.getEmail(),
                cliente.getNome(),
                codiceSpedizione
        );
    }
}
