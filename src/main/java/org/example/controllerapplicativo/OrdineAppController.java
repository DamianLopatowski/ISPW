package org.example.controllerapplicativo;

import org.example.bean.ProdottoBean;
import org.example.dao.OrdineDAOImpl;
import org.example.dao.PagamentoDAO;
import org.example.dao.PagamentoDAOImpl;
import org.example.dao.ProdottoDAO;
import org.example.dao.ProdottoDAOImpl;
import org.example.facade.ClienteFacade;
import org.example.model.Cliente;
import org.example.model.Ordine;
import org.example.service.NavigationService;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdineAppController {

    private static final Logger LOGGER = Logger.getLogger(OrdineAppController.class.getName());

    private final NavigationService navigationService;

    public OrdineAppController(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    public String procediOrdine() {
        Cliente cliente = navigationService.getClienteLoggato();
        if (cliente == null) {
            LOGGER.severe("Errore: cliente nullo in OrdineAppController!");
            return "Errore: cliente non loggato.";
        }

        Map<ProdottoBean, Integer> carrello = new HashMap<>(SessionController.getCarrello());
        boolean isOnline = SessionController.getIsOnlineModeStatic();

        if (carrello.isEmpty()) {
            LOGGER.warning("Carrello vuoto, nessun ordine da processare.");
            return "Il carrello è vuoto, nessun ordine da inviare.";
        }

        double totale = 0.0;
        for (Map.Entry<ProdottoBean, Integer> entry : carrello.entrySet()) {
            ProdottoBean prodotto = entry.getKey();
            int q = entry.getValue();

            double subtotale = prodotto.getPrezzoVendita() * q;
            totale += subtotale;

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info(String.format("- %s x%d → €%.2f", prodotto.getNome(), q, subtotale));
            }
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(String.format("Totale: €%.2f", totale));
        }

        Ordine ordine = Ordine.creaDaBean(cliente, carrello, totale);
        new OrdineDAOImpl(isOnline).salvaOrdine(ordine);

        ProdottoDAO prodottoDAO = new ProdottoDAOImpl(isOnline);
        for (Map.Entry<ProdottoBean, Integer> entry : carrello.entrySet()) {
            prodottoDAO.riduciQuantita(entry.getKey().getId(), entry.getValue());
        }

        PagamentoDAO pagamentoDAO = new PagamentoDAOImpl(isOnline);
        ClienteFacade facade = new ClienteFacade(pagamentoDAO);
        facade.inviaEmailRiepilogoOrdine(cliente, ordine.getProdotti());

        SessionController.svuotaCarrello();

        LOGGER.info("Ordine confermato per il cliente: " + cliente.getUsername());
        return "Ordine inviato correttamente!";
    }
}
